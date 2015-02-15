package com.mxsecurity.util;

/**
 * Interface for entities interested in following progress
 */
public interface Progress {
    /**
     * Called to report the current progress.
     *
     * @param value the current progress. The range of possible values
     *        depends on the class reporting the progress.
     */
    public void progress(long value);
}
