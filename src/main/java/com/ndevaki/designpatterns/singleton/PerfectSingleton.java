package com.ndevaki.designpatterns.singleton;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Making singleton perfect from serializable,threads and reflection
public class PerfectSingleton implements Serializable {

    private static volatile PerfectSingleton singleton=null;

    private static final Lock obj=new ReentrantLock(true);
    private PerfectSingleton(){
        //prevent from using refelction api
       if(singleton!=null){
           throw new RuntimeException("Cannot call thru reflection api");
       }
    }

    public static PerfectSingleton getInstance() throws InterruptedException {
        //lazy initialization
        //avoid poor performence
        if(singleton==null){
            //Rentrant lock better than synchronization, if many initial requests come at a time
            //in the begining, instead of making all threads waiting, exception will be thrown.
            obj.tryLock(1000, TimeUnit.MILLISECONDS);
                if(singleton==null) {
                    singleton = new PerfectSingleton();
                }
            obj.unlock();
        }
        return singleton;
    }

    //Make singleton from serialize and deserialize operation.
    protected PerfectSingleton readResolve() throws InterruptedException {
        return getInstance();
    }
}
