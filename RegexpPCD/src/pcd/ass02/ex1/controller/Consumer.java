package pcd.ass02.ex1.controller;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.view.RegexpView;

public class Consumer extends Thread {

	private final BlockingQueue<SearchFileResult> queue;
	private final RegexpView view;
	
	private final int nFiles;
	private int leastOneMatchRate;
	private double meanNumberOfMatches;
	
	public Consumer(final BlockingQueue<SearchFileResult> queue, final RegexpView view, final int nFiles) {
		this.queue = queue;
		this.view = view;
		this.nFiles = nFiles;
		this.leastOneMatchRate = 0;
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
					if (nMatches.get() > 0) {
						this.leastOneMatchRate++;
					}
					this.meanNumberOfMatches += (nMatches.get() - this.meanNumberOfMatches) / nComputedFiles;
				} else {
					// notify view file not found
				}
				// view
			} catch (InterruptedException ie) {
				//view.showAlert("Thread error", "Someone killed the consumer when was waiting for something. Please reset.\n\n" + ie.getMessage());
			}	
		}
	}
}
