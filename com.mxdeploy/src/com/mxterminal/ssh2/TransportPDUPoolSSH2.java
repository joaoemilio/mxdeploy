package com.mxterminal.ssh2;

/**
 * Implements a pool of PDUs which can be reused. This class holds a
 * pool of PDUs and tries to reuse them whenever possible.
 */
public class TransportPDUPoolSSH2 extends TransportPDUSSH2 {

    static int POOL_SIZE = 32;

    protected class PoolPDU extends TransportPDUSSH2 {
        protected PoolPDU(int pktType, int bufSize) {
            super(pktType, bufSize);
        }
        public void release() {
            this.reset();
            this.pktSize = 0;
            releasePDU(this);
        }
    }

    int cnt;

    TransportPDUSSH2[] pool;

    protected TransportPDUPoolSSH2() {
        pool = new TransportPDUSSH2[POOL_SIZE];
        cnt  = 0;
    }

    protected TransportPDUSSH2 createPDU(int bufSize) {
        return createPDU(0, bufSize);
    }

    protected TransportPDUSSH2 createPDU(int pktType, int bufSize) {
        synchronized(pool) {
            if (cnt == 0) {
                return new PoolPDU(pktType, bufSize);
            }
            int best = -1;
            for (int i = 0; i < cnt; i++) {
                if (pool[i].getMaxSize() >= bufSize
                    &&  (best < 0
                         || pool[i].getMaxSize() < pool[best].getMaxSize())) {
                    best = i;
                }
            }
            if (best >= 0) {
                TransportPDUSSH2 b = pool[best];
                b.pktType = pktType;
                pool[best] = pool[cnt-1];
                cnt--;
                return b;
            } else {
                return new PoolPDU(pktType, bufSize);
            }
        }
    }

    /**
     * Internal class which releases an incoming PDU.
     */
    protected void releasePDU(PoolPDU pdu) {
        synchronized(pool) {
            if(cnt < pool.length) {
                pool[cnt++] = pdu;
            } else {
                /* stat */
            }
        }
    }
}
