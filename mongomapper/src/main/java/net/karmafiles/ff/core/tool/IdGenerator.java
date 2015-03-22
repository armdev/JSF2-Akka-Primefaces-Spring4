/*
 * Copyright 2011 Dadastream. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY DADASTREAM ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL DADASTREAM OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Dadastream.
 *
 * @author Ilya Brodotsky
 * @author Timur Evdokimov
 */

package net.karmafiles.ff.core.tool;

import org.apache.commons.codec.binary.Base64;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

	private static AtomicLong atomicLong;

    private static SecureRandom mySecureRand;

    static {

	    atomicLong = new AtomicLong();
        mySecureRand = new SecureRandom();

    }

    private IdGenerator() {

    }

	private String getRandomGUID() {
        try {
            long time = System.currentTimeMillis();
            long rand = mySecureRand.nextLong();

	        // paranoid abs 
	        BigInteger bigInteger = new BigInteger("" + rand + Math.abs(time));

	        byte[] array = bigInteger.toByteArray();
	        return Base64.encodeBase64URLSafeString(array);

        } catch (Throwable t) {
            System.out.println("Error:" + t);
			throw new RuntimeException(t);	        
        }
    }
    
    public static String createSecureId() {
        return (new IdGenerator()).getRandomGUID();
    }

	public static String createNextSecureId() {
		IdGenerator idGenerator = new IdGenerator();
		return String.format("%019d-%s", atomicLong.incrementAndGet(), idGenerator.getRandomGUID());
	}
}