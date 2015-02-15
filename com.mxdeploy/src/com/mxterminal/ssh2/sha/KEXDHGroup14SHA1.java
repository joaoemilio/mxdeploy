package com.mxterminal.ssh2.sha;

import java.math.BigInteger;

import org.apache.log4j.Logger;

import com.mxsecurity.jca.KeyFactory;
import com.mxsecurity.jca.KeyPair;
import com.mxsecurity.jca.KeyPairGenerator;
import com.mxsecurity.jca.MessageDigest;
import com.mxsecurity.jce.KeyAgreement;
import com.mxsecurity.jce.interfaces.DHPrivateKey;
import com.mxsecurity.jce.interfaces.DHPublicKey;
import com.mxsecurity.jce.spec.DHParameterSpec;
import com.mxsecurity.jce.spec.DHPublicKeySpec;
import com.mxterminal.ssh2.DataBufferSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.ssh2.TransportSSH2;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.FatalExceptionSSH2;
import com.mxterminal.ssh2.exception.KEXFailedExceptionSSH2;
import com.mxterminal.ssh2.key.KeyExchanger;


/**
 * Implements diffie hellman key exchange using a predefined group. This
 * algorithm is known as 'diffie-hellman-group14-sha1'
 */
public class KEXDHGroup14SHA1 extends KeyExchanger {
	static Logger logger = Logger.getLogger(KEXDHGroup14SHA1.class);
    public final static BigInteger group14P =
        com.mxsecurity.publickey.ModPGroups.oakleyGroup14P;

    public final static BigInteger group14G =
        com.mxsecurity.publickey.ModPGroups.oakleyGroup14G;

    protected TransportSSH2 transport;
    protected DHPublicKey   dhPublicKey;
    protected DHPrivateKey  dhPrivateKey;
    protected byte[]        serverHostKey;
    protected BigInteger    serverF;
    protected BigInteger    clientE;
    protected byte[]        sharedSecret_K;
    protected byte[]        exchangeHash_H;
    protected MessageDigest sha1;

    public void init(TransportSSH2 transport) throws ExceptionSSH2 {
        this.transport = transport;
        this.sha1      = createHash();

        DHParameterSpec group14Params = new DHParameterSpec(group14P, group14G);
        generateDHKeyPair(group14Params);

        if(!transport.isServer()) {
            sendDHINIT(SSH2.MSG_KEXDH_INIT);
        }
    }

    public void processKEXMethodPDU(TransportPDUSSH2 pdu) throws ExceptionSSH2 {
        if(pdu.getType() == SSH2.MSG_KEXDH_REPLY) {
            if(transport.isServer()) {
                throw new KEXFailedExceptionSSH2("Unexpected KEXDH_REPLY");
            }

            serverHostKey      = pdu.readString();
            serverF            = pdu.readBigInt();
            byte[] serverSigH  = pdu.readString();

            DHPublicKeySpec srvPubSpec = new DHPublicKeySpec
                (serverF, group14P, group14G);
            
            computeSharedSecret_K(srvPubSpec);
            computeExchangeHash_H();

            transport.authenticateHost(serverHostKey, serverSigH,
                                       exchangeHash_H);

            transport.sendNewKeys();

        } else if(pdu.getType() == SSH2.MSG_KEXDH_INIT) {
            // !!! TODO SERVER
        }
    }

    public MessageDigest getExchangeHashAlgorithm() {
        sha1.reset();
        return sha1;
    }

    public byte[] getSharedSecret_K() {
        DataBufferSSH2 buf = new DataBufferSSH2(1024);
        buf.writeString(sharedSecret_K);
        return buf.readRestRaw();
    }

    public byte[] getExchangeHash_H() {
        return exchangeHash_H;
    }

    public String getHostKeyAlgorithms() {
        return "ssh-dss,ssh-rsa";
    }

    protected void computeExchangeHash_H() {
        DataBufferSSH2 buf = new DataBufferSSH2(64*1024);

        if(transport.isServer()) {
            serverF = dhPublicKey.getY();
        } else {
            clientE = dhPublicKey.getY();
        }

        buf.writeString(transport.getClientVersion());
        buf.writeString(transport.getServerVersion());
        buf.writeString(transport.getClientKEXINITPDU().getData(),
                        transport.getClientKEXINITPDU().getPayloadOffset(),
                        transport.getClientKEXINITPDU().getPayloadLength());
        buf.writeString(transport.getServerKEXINITPDU().getData(),
                        transport.getServerKEXINITPDU().getPayloadOffset(),
                        transport.getServerKEXINITPDU().getPayloadLength());
        buf.writeString(serverHostKey);
        buf.writeBigInt(clientE);
        buf.writeBigInt(serverF);
        buf.writeString(sharedSecret_K);

        sha1.reset();
        sha1.update(buf.getData(), 0, buf.getWPos());
        exchangeHash_H = sha1.digest();

//        transport.getLog().debug2("SSH2KEXDHGroup14SHA1",
//                                  "computeExchangeHash_H", "E: ",
//                                  clientE.toByteArray());
//        transport.getLog().debug2("SSH2KEXDHGroup14SHA1",
//                                  "computeExchangeHash_H", "F: ",
//                                  serverF.toByteArray());
//        transport.getLog().debug2("SSH2KEXDHGroup14SHA1",
//                                  "computeExchangeHash_H", "K: ",
//                                  sharedSecret_K);
//        transport.getLog().debug2("SSH2KEXDHGroup14SHA1",
//                                  "computeExchangeHash_H", "Hash over: ",
//                                  buf.getData(), 0, buf.getWPos());
//        transport.getLog().debug2("SSH2KEXDHGroup14SHA1",
//                                  "computeExchangeHash_H", "H: ",
//                                  exchangeHash_H);
    }

    protected void computeSharedSecret_K(DHPublicKeySpec peerPubSpec)
    throws ExceptionSSH2 {
        try {
            KeyFactory   dhKeyFact = KeyFactory.getInstance("DH");
            KeyAgreement dhKEX     = KeyAgreement.getInstance("DH");

            DHPublicKey  peerPubKey =
                (DHPublicKey)dhKeyFact.generatePublic(peerPubSpec);

            dhKEX.init(dhPrivateKey);
            dhKEX.doPhase(peerPubKey, true);

            sharedSecret_K = dhKEX.generateSecret();
        } catch (Exception e) {

            e.printStackTrace();

            throw new FatalExceptionSSH2("Error computing shared secret: "
                                         + e);
        }
    }

    protected void sendDHINIT(int type) throws ExceptionSSH2 {
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(type);

        pdu.writeBigInt(dhPublicKey.getY());

        transport.transmitInternal(pdu);
    }

    protected MessageDigest createHash() throws ExceptionSSH2 {
        try {
            return MessageDigest.getInstance("SHA1");
        } catch (Exception e) {
            throw new KEXFailedExceptionSSH2("SHA1 not implemented", e);
        }
    }

    protected void generateDHKeyPair(DHParameterSpec dhParams) throws ExceptionSSH2 {
        try {
            KeyPairGenerator dhKeyPairGen = KeyPairGenerator.getInstance("DH");
            
            dhKeyPairGen.initialize(dhParams, transport.getSecureRandom());

            KeyPair dhKeyPair = dhKeyPairGen.generateKeyPair();

            dhPrivateKey = (DHPrivateKey)dhKeyPair.getPrivate();
            dhPublicKey  = (DHPublicKey)dhKeyPair.getPublic();
        } catch (Exception e) {
            throw new FatalExceptionSSH2("Error generating DiffieHellman keys: "
                                         + e);
        }
    }

}
