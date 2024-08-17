package nel.marco;


import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Dispatchers;
import org.example.KotlinClass;

public class JavaClass {
    public static void main(String[] args) {
        System.out.println("Hello from java!");
        new JavaClass();
    }

    public JavaClass(){
        request();
        suspendFunction();
    }


    void request() {
        var clazz = new KotlinClass();
        //normal kotlin function
        System.out.println(clazz.sendRequest());
    }

    void suspendFunction() {
        var clazz = new KotlinClass();
        try {
            var x = BuildersKt.runBlocking(
                    Dispatchers.getDefault(),//context to be ran on
                    (coroutineScope, continuation) -> clazz.suspendCode(continuation)
            );

            System.out.println(x);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

