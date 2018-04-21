package pcd.ass02.ex2.model;

import io.vertx.core.Vertx;

public class Launcher {
	
	public static void main(String[] args) {
		final Vertx vertx = Vertx.vertx();
		final MainVerticle myVerticle = new MainVerticle();
		vertx.deployVerticle(myVerticle);
	}

}
