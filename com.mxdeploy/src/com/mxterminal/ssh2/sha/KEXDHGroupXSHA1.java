package com.mxterminal.ssh2.sha;

import java.math.BigInteger;

import com.mxsecurity.jce.spec.DHParameterSpec;
import com.mxsecurity.jce.spec.DHPublicKeySpec;
import com.mxterminal.ssh2.DataBufferSSH2;
import com.mxterminal.ssh2.PreferencesSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.ssh2.TransportSSH2;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.KEXFailedExceptionSSH2;

/**
 * Implements diffie hellman key exchange with group negotiation. This
 * algorithm is known as 'diffie-hellman-group-exchange-sha1'
 */
public class KEXDHGroupXSHA1 extends KEXDHGroup1SHA1 {

    private final static int MIN_BITS = 1024;
    private final static int MAX_BITS = 8192;

    private int        reqBits;
    private BigInteger p;
    private BigInteger g;

    public void init(TransportSSH2 transport) throws ExceptionSSH2 {
        this.transport = transport;
        this.sha1      = createHash();
        if(!transport.isServer()) {
            sendGEXRequest();
        }
    }

    public void processKEXMethodPDU(TransportPDUSSH2 pdu) throws ExceptionSSH2 {
        switch(pdu.getType()) {
        case SSH2.MSG_KEXDH_GEX_REQUEST_OLD:
            // !!! TODO
            break;

        case SSH2.MSG_KEXDH_GEX_REQUEST:
            // !!! TODO
            break;

        case SSH2.MSG_KEXDH_GEX_GROUP:
            if(transport.isServer()) {
                throw new KEXFailedExceptionSSH2("Unexpected KEXDH_GEX_GROUP");
            }
            p = pdu.readBigInt();
            g = pdu.readBigInt();

//            DHParameterSpec dhParams = null;
//			try {
//				AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
//				paramGen.init(512);
//				AlgorithmParameters params = paramGen.generateParameters();
//				dhParams =  (DHParameterSpec) params.getParameterSpec(DHParameterSpec.class);
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			} catch (InvalidParameterSpecException e) {
//				e.printStackTrace();
//			} 
            
            DHParameterSpec dhParams = new DHParameterSpec(p, g);
            generateDHKeyPair(dhParams);

            sendDHINIT(SSH2.MSG_KEXDH_GEX_INIT);
            break;

        case SSH2.MSG_KEXDH_GEX_INIT:
            // !!! TODO
            break;

        case SSH2.MSG_KEXDH_GEX_REPLY:
            if(transport.isServer()) {
                throw new KEXFailedExceptionSSH2("Unexpected KEXDH_GEX_REPLY");
            }
            serverHostKey      = pdu.readString();
            serverF            = pdu.readBigInt();
            byte[] serverSigH  = pdu.readString();

            DHPublicKeySpec srvPubSpec = new DHPublicKeySpec(serverF, p, g);

            computeSharedSecret_K(srvPubSpec);
            computeExchangeHash_H();

            transport.authenticateHost(serverHostKey, serverSigH,
                                       exchangeHash_H);

            transport.sendNewKeys();
            break;
        default:
            throw new KEXFailedExceptionSSH2("Unexpected KEXDH reply: " + pdu.getType());
        }
    }

    protected void computeExchangeHash_H() {
        DataBufferSSH2 buf = new DataBufferSSH2(8192);

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
        if (transport.incompatibleOldDHGex) {
            buf.writeInt(reqBits);
        } else {
            buf.writeInt(MIN_BITS);
            buf.writeInt(reqBits);
            buf.writeInt(MAX_BITS);
        } 
        buf.writeBigInt(p);
        buf.writeBigInt(g);
        buf.writeBigInt(clientE);
        buf.writeBigInt(serverF);
        buf.writeString(sharedSecret_K);

        sha1.reset();
        sha1.update(buf.getData(), 0, buf.getWPos());
        exchangeHash_H = sha1.digest();

//        transport.getLog().debug2("SSH2KEXDHGroup1SHA1",
//                                  "computeExchangeHash_H", "E: ",
//                                  clientE.toByteArray());
//        transport.getLog().debug2("SSH2KEXDHGroup1SHA1",
//                                  "computeExchangeHash_H", "F: ",
//                                  serverF.toByteArray());
//        transport.getLog().debug2("SSH2KEXDHGroup1SHA1",
//                                  "computeExchangeHash_H", "K: ",
//                                  sharedSecret_K);
//        transport.getLog().debug2("SSH2KEXDHGroup1SHA1",
//                                  "computeExchangeHash_H", "Hash over: ",
//                                  buf.getData(), 0, buf.getWPos());
//        transport.getLog().debug2("SSH2KEXDHGroup1SHA1",
//                                  "computeExchangeHash_H", "H: ",
//                                  exchangeHash_H);
    }

    protected void sendGEXRequest() throws ExceptionSSH2 {
        TransportPDUSSH2 pdu;
        reqBits = estimateGroupBits();

        if (transport.incompatibleOldDHGex) {
            pdu = TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_KEXDH_GEX_REQUEST_OLD);            
            pdu.writeInt(reqBits);
        } else {
            pdu = TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_KEXDH_GEX_REQUEST);            
            pdu.writeInt(MIN_BITS);
            pdu.writeInt(reqBits);
            pdu.writeInt(MAX_BITS);
        }
        transport.transmitInternal(pdu);
    }

    /*
     * Estimates the group order for a Diffie-Hellman group that has an attack
     * complexity approximately the same as O(2**bits) where bits is the longest
     * of transmitter and receiver cipher keys.
     *
     * Estimate with: O(exp(1.9223 * (ln q)^(1/3) (ln ln q)^(2/3)))
     *
     * (This code snippet is from OpenSSH2)
     */
    private int estimateGroupBits() {
        PreferencesSSH2 pref = transport.getOurPreferences();
        int keylens[] = {
                PreferencesSSH2.getCipherKeyLen(pref.getAgreedCipher(true, false)),
                PreferencesSSH2.getCipherKeyLen(pref.getAgreedCipher(false, false)),
                PreferencesSSH2.getMacKeyLen(pref.getAgreedMac(true, false)),
                PreferencesSSH2.getMacKeyLen(pref.getAgreedMac(false, false))
            };
        int bits = 0;
        for (int i=0; i<keylens.length; i++)
            if (keylens[i] > bits)
                bits = keylens[i];
        bits *= 8;

        if (bits < 128)
            return (1024);	/* O(2**86) */
        if (bits < 192)
            return (2048);	/* O(2**116) */
        return (4096);		/* O(2**156) */
    }

}
