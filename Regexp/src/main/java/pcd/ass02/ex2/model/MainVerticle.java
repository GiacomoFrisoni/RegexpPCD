package pcd.ass02.ex2.model;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
    	
    	final FileSystem fs = vertx.fileSystem();
    	
    	fs.readFile("build.gradle", event -> {
    		AsyncResult<Buffer> res = (AsyncResult<Buffer>)event;
    		System.out.println("res: " + res.result() + "\n" + res.getClass());
    	});
    	
    	System.out.println("ciao");
    	
    }
}
