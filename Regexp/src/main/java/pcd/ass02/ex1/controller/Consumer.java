package pcd.ass02.ex1.controller;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.view.RegexpView;

/**
 * This class models a Regexp Consumer.
 * It picks up the results of the task computations from the buffer, calculates some statistics
 * and shows them on video as soon as they are available.
 *
 */
public class Consumer extends Thread {

	private final BlockingQueue<SearchFileResult> queue;
	private final RegexpView view;
	
	private final int nFiles;
	private int nLeastOneMatch;
	private double meanNumberOfMatches;
	
	/**
	 * Constructs a new Consumer.
	 * 
	 * @param queue
	 * 		the producer / consumer queue
	 * @param view
	 * 		the application view
	 * @param nFiles
	 * 		the total number of files to compute
	 */
	public Consumer(final BlockingQueue<SearchFileResult> queue, final RegexpView view, final int nFiles) {
		this.queue = queue;
		this.view = view;
		this.nFiles = nFiles;
		this.nLeastOneMatch = 0;
		this.meanNumberOfMatches = 0;
	}
	
	@Override
	public void run() {
		int nComputedFiles = 0;
		SearchFileResult res;
		while (nComputedFiles < this.nFiles) {
			try {
				res = this.queue.take();
				nComputedFiles++;
				final Optional<Integer> nMatches = res.getNumberOfMatches();
				if (nMatches.isPresent()) {
					// Updates the number of file with matches
					if (nMatches.get() > 0) {
						this.nLeastOneMatch++;
						// Updates the mean number of matches among files with matches
						double tmp = this.meanNumberOfMatches;
						this.meanNumberOfMatches += (nMatches.get() - tmp) / nComputedFiles;
					}
					// Shows file result on view
					this.view.addResult(res.getPath().toString(), nMatches.get());
				} else {
					// Shows file result on view
					this.view.addResult(res.getPath().toString(), res.getMessage().get());
				}
				// Shows statistics on view
				this.view.showMeanNumberOfMatches(this.meanNumberOfMatches);
				this.view.showLeastOneMatchPercentage((double)this.nLeastOneMatch / (double)nComputedFiles);
				// Shows analysis progress on view
				this.view.setNumberOfScannedFiles(nComputedFiles);
			} catch (final InterruptedException ie) {
				this.view.showThreadException("Someone interrupted the consumer when was waiting for something", ie);
			}	
		}

		System.out.println(nComputedFiles);
	}
	
}
