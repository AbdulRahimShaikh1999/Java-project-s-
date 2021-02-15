
package rockpaperscissorslizardspock;


import java.util.concurrent.Callable;

public class MakeMove implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return (int)(Math.ceil(Math.random() * 5));
    }

}
