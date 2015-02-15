package com.mxsecurity.cipher;


import com.mxsecurity.jca.InvalidKeyException;
import com.mxsecurity.jca.Key;
import com.mxsecurity.jca.SecureRandom;
import com.mxsecurity.jca.spec.AlgorithmParameterSpec;
import com.mxsecurity.jce.CipherSpi;
import com.mxsecurity.jce.spec.SecretKeySpec;




public final class ArcFour extends CipherSpi {
    int    x;
    int    y;
    int[]  state = new int[256];

    int arcfour_byte() {
        int x;
        int y;
        int sx, sy;
        x = (this.x + 1) & 0xff;
        sx = state[x];
        y = (sx + this.y) & 0xff;
        sy = state[y];
        this.x = x;
        this.y = y;
        state[y] = (sx & 0xff);
        state[x] = (sy & 0xff);
        return state[((sx + sy) & 0xff)];
    }

    protected int engineDoFinal(byte[] input, int inputOffset, int inputLen,
                                byte[] output, int outputOffset) {
        int end = inputOffset + inputLen;
        for(int si = inputOffset, di = outputOffset; si < end; si++, di++)
            output[di] = (byte)(((int)input[si] ^ arcfour_byte()) & 0xff);
        return inputLen;
    }

    public void initializeKey(byte[] key) {
        int t, u = 0;
        int keyindex;
        int stateindex;
        int counter;

        for(counter = 0; counter < 256; counter++)
            state[counter] = (byte)counter;
        keyindex = 0;
        stateindex = 0;
        for(counter = 0; counter < 256; counter++) {
            t = state[counter];
            stateindex = (stateindex + key[keyindex] + t) & 0xff;
            u = state[stateindex];
            state[stateindex] = t;
            state[counter] = u;
            if(++keyindex >= key.length)
                keyindex = 0;
        }
    }

    protected int engineGetBlockSize() {
        return 1;
    }

    protected byte[] engineGetIV() {
        return null;
    }

    protected int engineGetOutputSize(int inputLen) {
        return inputLen;
    }

    protected void engineInit(int opmode, Key key,
                              AlgorithmParameterSpec params,
                              SecureRandom random)
    throws InvalidKeyException {
        initializeKey(((SecretKeySpec)key).getEncoded());
    }

    protected void engineInit(int opmode, Key key,
                              SecureRandom random) throws InvalidKeyException {
        engineInit(opmode, key, (AlgorithmParameterSpec)null, random);
    }

    protected void engineSetMode(String mode) {}

    protected void engineSetPadding(String padding) {}

}
