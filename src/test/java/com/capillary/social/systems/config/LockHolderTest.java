package com.capillary.social.systems.config;

import com.google.common.annotations.VisibleForTesting;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This code is intellectual property of Capillary Technologies.
 * <p>
 * Copyright (c) (2017)
 * Created By able
 * Created On 17/10/17
 */
public class LockHolderTest {
	RuntimeException ex;

	@Before
	public void setUp()
	{
		ex=null;
	}

	@Test
	public void lockTest() throws InterruptedException {
		Runnable r= () -> {
			try {
				LockHolder.lock("s1");
				Thread.sleep(1000L);
				LockHolder.release("s1");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				ex = e;
			}

		};
		Thread t1= new Thread(r);
		Thread t2= new Thread(r);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		Assert.assertNotNull(ex);
		Assert.assertEquals(ex.getMessage(),"could not get the lock");
	}
	@Test
	public void lockTestSingle() throws InterruptedException {
		Runnable r= () -> {
			try {
				LockHolder.lock("s1");
				Thread.sleep(1000L);
				LockHolder.release("s1");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				ex = e;
			}

		};
		Thread t1= new Thread(r);
		Thread t2= new Thread(r);
		t1.start();
		t1.join();
		t2.start();
		t2.join();
		Assert.assertNull(ex);
	}

}