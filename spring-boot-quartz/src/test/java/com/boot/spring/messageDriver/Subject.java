package com.boot.spring.messageDriver;

import java.util.Arrays;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

/**
 * @author yaoqiang
 *消息主题
 */
public class Subject  {
   public static void main(String[] args){
	// Create a publisher.

    SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

    // Create a subscriber and register it with the publisher.

    MySubscriber<String> subscriber = new MySubscriber<>();

    publisher.subscribe(subscriber);
    // Publish several data items and then close the publisher.

    System.out.println("Publishing data items...");
    String[] items = { "jan", "feb", "mar", "apr", "may", "jun",
                       "jul", "aug", "sep", "oct", "nov", "dec" };
    Arrays.asList(items).stream().forEach(i -> publisher.submit(i));
    publisher.close();

    try
    {
       synchronized("A")
       {
          "A".wait();
       }
       System.out.println("end");
    }
    catch (InterruptedException ie)
    {
    }
 }
	

}

class MySubscriber<T> implements Subscriber<T>
{
   private Subscription subscription;

   @Override
   public void onSubscribe(Subscription subscription)
   {
      this.subscription = subscription;
      subscription.request(100);
   }

   @Override
   public void onNext(T item)
   {
      System.out.println("Received: " + item);
   }

   @Override
   public void onError(Throwable t)
   {
      t.printStackTrace();
      synchronized("A")
      {
         "A".notifyAll();
      }
   }

   @Override
   public void onComplete()
   {
      System.out.println("Done");
      synchronized("A")
      {
         "A".notifyAll();
      }
   }
}
