package pcd.ass02.ex1.controller;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import pcd.ass02.ex1.model.SearchFileErrorResult;
import pcd.ass02.ex1.model.SearchFileResult;
import pcd.ass02.ex1.model.SearchFileSuccessfulResult;
import pcd.ass02.ex1.view.MessageUtils.ExceptionType;
import pcd.ass02.ex1.view.RegexpView;

/**
 * This class models a Regexp Consumer.
 * It picks up the results of the task computations from the buffer, calculates some statistics
 * and shows them on video as soon as they are available.
 *
 */
public class Consumer extends Thread {
	
	public static final Optional<SearchFileResult> POISON_PILL = Optional.empty();
	
	private final BlockingQueue<Optional<SearchFileResult>> queue;
	private final RegexpView view;
	
	private int nLeastOneMatch;
	private double meanNumberOfMatches;
	private boolean foundedPoisonPill;
	
	/**
	 * Constructs a new Consumer.
	 * 
	 * @param queue
	 * 		the producer / consumer queue
	 * @param view
	 * 		the application view
	 */
	public Consumer(final BlockingQueue<Optional<SearchFileResult>> queue, final RegexpView view) {
		Objects.requireNonNull(queue);
		Objects.requireNonNull(view);
		this.queue = queue;
		this.view = view;
		this.nLeastOneMatch = 0;
		this.meanNumberOfMatches = 0;
		this.foundedPoisonPill = false;
	}
	
	@Override
	public void run() {
		int nComputedFiles = 0;
		Optional<SearchFileResult> element;
		while (!this.foundedPoisonPill) {
			try {
				element = this.queue.take();
				// Verify that the element is not a poison pill
				if (element.equals(POISON_PILL)) {
					this.foundedPoisonPill = true;
				} else {
					if (element.isPresent()) {
						final SearchFileResult res = element.get();
						nComputedFiles++;
						// Checks the type of the result
						if (res instanceof SearchFileSuccessfulResult) {
							final SearchFileSuccessfulResult successfulRes = (SearchFileSuccessfulResult)res;
							final int nMatches = successfulRes.getNumberOfMatches();
							if (nMatches > 0) {
								// Updates the number of files with least one match
								this.nLeastOneMatch++;
								// Updates the mean number of matches among files with matches
								final double oldMean = this.meanNumberOfMatches;
								this.meanNumberOfMatches += (nMatches - oldMean) / this.nLeastOneMatch;
							}
							// Shows file result on view
							this.view.showResult(successfulRes.getPath().toString(), nMatches, successfulRes.getElapsedTime());
						} else if (res instanceof SearchFileErrorResult) {
							final SearchFileErrorResult errorRes = (SearchFileErrorResult)res;
							// Shows file result on view
							this.view.showResult(errorRes.getPath().toString(), errorRes.getErrorMessage());
						}
						// Shows statistics on view
						this.view.showMeanNumberOfMatches(this.meanNumberOfMatches);
						this.view.showLeastOneMatchPercentage((double)this.nLeastOneMatch / (double)nComputedFiles);
						// Shows analysis progress on view
						this.view.setNumberOfScannedFiles(nComputedFiles);
					}	
				}
			} catch (final InterruptedException ie) {
				this.view.showException(ExceptionType.THREAD_EXCEPTION, "Someone interrupted the consumer when was waiting for something", ie);
			}
		}
		this.view.setFinish();
	}
	
}
