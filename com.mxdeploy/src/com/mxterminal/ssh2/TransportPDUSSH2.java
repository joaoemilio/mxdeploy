package com.mxterminal.ssh2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mxsecurity.jce.Cipher;
import com.mxsecurity.jce.Mac;
import com.mxsecurity.jce.ShortBufferException;
import com.mxsecurity.util.SecureRandomAndPad;
import com.mxterminal.ssh2.exception.CompressionException;
import com.mxterminal.ssh2.exception.CorruptPacketException;
import com.mxterminal.ssh2.exception.EOFException;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.MacCheckExceptionSSH2;


/**
 * Handles one packet data unit. These are the actual packets which
 * are sent over en encrypted tunnel. Encryption and compression are
 * applied to these.
 * <p>
 * There is a difference bwteeen incoming and outgoing PDUs. The
 * latter have extra space allocated in them to help speed up the
 * encryption process.
 * <p>
 * This class makes an effort to avoid copying data wherever possible.
 */
public class TransportPDUSSH2 extends DataBufferSSH2 {
    public final static int PACKET_DEFAULT_SIZE = 256;
    public final static int PACKET_MIN_SIZE     = 16;
    public final static int PACKET_MAX_SIZE     = 35000;

    public static int pktDefaultSize = PACKET_DEFAULT_SIZE;

    // !!! public static SSH2TransportPDU factoryInstance = new SSH2TransportPDU();
    public static TransportPDUSSH2 factoryInstance = new TransportPDUPoolSSH2();

    byte[] macTmpBuf;

    protected int pktSize;
    protected int padSize;
    protected int pktType;

    protected TransportPDUSSH2() {
        /* Factory instance constructor */
    }

    /**
     * Constructor which creates a prefabricated packet.
     *
     * @param pktType Type of packet to create.
     * @param bufSize How many bytes it should have room for.
     */
    protected TransportPDUSSH2(int pktType, int bufSize) {
        super(bufSize);
        this.pktType   = pktType;
        this.pktSize   = 0;
        this.padSize   = 0;
        this.macTmpBuf = new byte[128];
    }

    /**
     * Create a new PDU to use for incoming packets.
     *
     * @param bufSize How many bytes it should have room for.
     */
    protected TransportPDUSSH2 createPDU(int bufSize) {
        return createPDU(0, bufSize);
    }

   /**
     * Create a new PDU for an outgoing packet.
     *
     * @param pktType Type of packet to create.
     * @param bufSize How many bytes it should have room for.
     */
    protected TransportPDUSSH2 createPDU(int pktType, int bufSize) {
        return new TransportPDUSSH2(pktType, bufSize);
    }

    /**
     * Register a factor which handles the creation and destruction of
     * incoming and outgoing packets.
     */
    public final static void setFactoryInstance(TransportPDUSSH2 factory) {
        factoryInstance = factory;
    }

    /**
     * Have the factory create a PDU for an incoming packet.
     *
     * @param bufSize How many bytes it should have room for.
     */
    public final static TransportPDUSSH2 createIncomingPacket(int bufSize) {
        return factoryInstance.createPDU(bufSize);
    }

    /**
     * Have the factory create a PDU for an incoming packet.
     */
    public final static TransportPDUSSH2 createIncomingPacket() {
        return createIncomingPacket(pktDefaultSize);
    }

    /**
     * Have the factory create a PDU for an outgoing packet.
     *
     * @param pktType Type of packet to create.
     * @param bufSize How many bytes it should have room for.
     */
    public final static TransportPDUSSH2 createOutgoingPacket(int pktType,
            int bufSize) {
        TransportPDUSSH2 pdu = factoryInstance.createPDU(pktType, bufSize);
        pdu.writeInt(0);  // dummy sequence number
        pdu.writeInt(0);  // dummy length
        pdu.writeByte(0); // dummy pad-length
        pdu.writeByte(pktType);
        return pdu;
    }

    /**
     * Have the factory create a PDU for an outgoing packet.
     *
     * @param pktType Type of packet to create.
     */
    public final static TransportPDUSSH2 createOutgoingPacket(int pktType) {
        return createOutgoingPacket(pktType, pktDefaultSize);
    }

    /**
     * Static function which creates a tranceiver context with the
     * mentioned algorithms.
     *
     * @param cipherName Name of cipher algorithm to use.
     * @param macName Name of message authentication cipher to use.
     * @param compName Name of compression algorithm to use.
     */
    public final static TransportSSH2.TranceiverContext
    createTranceiverContext(String cipherName, String macName,
                            String compName)
        throws Exception {
        return factoryInstance.createTranceiverContextImpl(cipherName, macName,
                compName);
    }

    /**
     * Release this PDU. This means that the PDU can be freed or
     * reused for another packet.
     */
    public void release() {}

    /**
     * Create a copy of this PDU.
     */
    public TransportPDUSSH2 makeCopy() {
        TransportPDUSSH2 copy = factoryInstance.createPDU(this.pktType,
                                                          this.data.length);
        System.arraycopy(this.data, 0, copy.data, 0, this.data.length);
        copy.pktSize = this.pktSize;
        copy.padSize = this.padSize;
        copy.rPos    = this.rPos;
        copy.wPos    = this.wPos;

        return copy;
    }

    public int getType() {
        return pktType;
    }

    public void setType(int pktType) {
        this.pktType = pktType;
    }

    /**
     * Get the length of the payload. The payload is the actual data
     * sent. Note that the payload may still be compressed.
     */
    public int getPayloadLength() {
        int plSz;
        if(pktSize == 0) {
            plSz = wPos - getPayloadOffset();
        } else {
            plSz = pktSize - padSize - 1;
        }
        return plSz;
    }

    /**
     * Get the offset in the data buffer where the payload starts.
     */
    public int getPayloadOffset() {
        return 4 + 4 + 1; // Skip sequence, length and padsize
    }

    /**
     * Read and decrypt an incoming packet from the given stream. This
     * function handles decryption, mac checking and uncompression.
     *
     * @param in Stream to read packet from.
     * @param seqNum Sequence number of packet.
     * @param context Tranceiver context.
     */
    public void readFrom(InputStream in, int seqNum,
                         TransportSSH2.TranceiverContext context)
    throws IOException, ExceptionSSH2, ShortBufferException {
        writeInt(seqNum);  // Not received, used for MAC calculation
        rPos = 4;          // Skip it also (i.e. we don't want to read it)
        int bs = 8;
        int macSize = 0;
        if(context.cipher != null) {
            bs = context.cipher.getBlockSize();
            bs = (bs > 8 ? bs : 8);
            readNextNFrom(in, bs);
			context.cipher.doFinal(data, 4, bs, data, 4);
        } else {
            readNextNFrom(in, 8);
        }
        bs -= 4; // The part of body pre-read above (i.e. subtract len-field)
        pktSize = readInt();

        if(context.mac != null) {
            macSize = context.mac.getMacLength();
        }

        int totPktSz = (pktSize + 4 + macSize);
        if(totPktSz > PACKET_MAX_SIZE || totPktSz < PACKET_MIN_SIZE) {
            throw new CorruptPacketException("Invalid packet size: " +
                                                 pktSize);
        }

        readNextNFrom(in, pktSize - bs); // Allready read bs bytes of body

        if(context.cipher != null) {
		   context.cipher.doFinal(data, 8 + bs, pktSize - bs, data, 8 + bs);
        }

        if(context.mac != null) {
            readNextNFrom(in, macSize);
            checkMac(context.mac, macSize);
        }

        padSize = readByte();

//        if(context.compressor != null) {
//            // Update pktSize so getPayloadLength() calculates right value
//            pktSize = context.compressor.uncompress(this, pktSize - padSize - 1);
//            pktSize += padSize + 1;
//        }

        pktType = readByte();
    }

    protected void checkMac(Mac mac, int macSize)
    throws MacCheckExceptionSSH2, ShortBufferException {
        mac.update(data, 0, 8 + pktSize);
        mac.doFinal(macTmpBuf, 0);
        int dOff = 8 + pktSize;

        for(int i = 0; i < macSize; i++) {
            if(macTmpBuf[i] != data[dOff++]) {
                throw new MacCheckExceptionSSH2("MAC check failed");
            }
        }
    }

    /**
     * Ensure that the buffer has room for at least n more bytes.
     *
     * @param n Number of bytes we need room for
     */
    private void ensureSize(int n) {
        if((data.length - wPos) < n) {
            byte[] tmp = data;
            int  newSz;
            if (wPos+n < 1024 && wPos+n < data.length*2) {
                newSz = data.length * 2;
            } else {
                newSz = wPos + n + 1024;
            }
            data = new byte[newSz];
            System.arraycopy(tmp, 0, data, 0, tmp.length);
        }
    }

    /**
     * Read a number of bytes from the stream and store in the
     * internal buffer. This function may resize the buffer if needed.
     *
     * @param in Stream to read data from.
     * @param n Number of bytes to read.
     */
    protected final void readNextNFrom(InputStream in, int n)
        throws IOException, EOFException {
        ensureSize(n);
        n += wPos;
        while(wPos < n) {
            int s = in.read(data, wPos, n - wPos);
            if(s == -1)
                throw new EOFException("Server closed connection");
            wPos += s;
        }
    }

    public final void writeByte(int b) {
        ensureSize(1);
        data[wPos++] = (byte)b;
    }

    public final void writeString(byte[] str, int off, int len) {
        ensureSize(len + 4);
        super.writeString(str, off, len);
    }

    public final void writeRaw(byte[] raw, int off, int len) {
        ensureSize(len);
        super.writeRaw(raw, off, len);
    }

    /**
     * Encrypts and writes an outgoing packet to the stream. This
     * function handles compression, mac calculation and encryption.
     *
     * @param out Stream to write resulting data to.
     * @param seqNum Sequence number of packet.
     * @param context Tranceiver context to use.
     * @param rand An object from which random numbers and padding
     * data is read.
     */
    public void writeTo(OutputStream out, int seqNum,
                        TransportSSH2.TranceiverContext context,
                        SecureRandomAndPad rand)
    throws IOException, ShortBufferException, CompressionException {
        int macSize = 0;
        int bs      = 8;

//        if(context.compressor != null) {
//            context.compressor.compress(this);
//        }

        if(context.cipher != null) {
            bs = context.cipher.getBlockSize();
            bs = (bs > 8 ? bs : 8);
        }

        // Subtract dummy sequence number since it is not sent
        //
        padSize = bs - ((wPos - 4) % bs);
        if(padSize < 4)
            padSize += bs;

        // sequence + length fields not counted in packet-length
        //
        pktSize = wPos + padSize - 8;
        ensureSize(padSize);
        rand.nextPadBytes(data, wPos, padSize);

        wPos = 0;
        writeInt(seqNum); // Not transmitted, used for MAC calculation
        writeInt(pktSize);
        writeByte(padSize);
        int totPktSz = pktSize + 4; // packet size including length field

        if(context.mac != null) {
            // The MAC is calculated on full packet including sequence number
            //
            int macOffset = 4 + totPktSz;
            context.mac.update(data, 0, macOffset);
            context.mac.doFinal(data, macOffset);
            macSize = context.mac.getMacLength();
        }

        if(context.cipher != null) {
			context.cipher.doFinal(data, 4, totPktSz, data, 4);
        }
        out.write(data, 4, totPktSz + macSize);
        release();
    }

    /**
     * Creates a string representation of this PDU.
     */
    public String toString() {
        return "pdu: buf-sz = " + data.length +
               ", rPos = " + rPos +
               ", wPos = " + wPos +
               ", pktSize = " + pktSize +
               ", padSize = " + padSize +
               ", pktType = " + pktType;
    }

    /**
     * An implementation which actually creates a tranceiver context.
     *
     * @param cipherName Name of cipher algorithm to use.
     * @param macName Name of message authentication cipher to use.
     * @param compName Name of compression algorithm to use.
     */
    public TransportSSH2.TranceiverContext
    createTranceiverContextImpl(String cipherName, String macName,
                                String compName)
    throws Exception {
        TransportSSH2.TranceiverContext ctx =
            new TransportSSH2.TranceiverContext();

        if(!"none".equals(cipherName)) {
            ctx.cipher = Cipher.getInstance(cipherName);
        }
        if(!"none".equals(macName)) {
            ctx.mac = Mac.getInstance(macName);
        }
//        if(!"none".equals(compName)) {
//            ctx.compressor = CompressorSSH2.getInstance(compName);
//        }

        return ctx;
    }
}
