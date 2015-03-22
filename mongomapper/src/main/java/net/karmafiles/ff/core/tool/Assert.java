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

import java.util.Collection;

public final class Assert {

	private Assert() {
		// noninstantiable
	}

	public static void notNull(Object o) {
		notNull(o, null);
	}

	public static void notNull(Object o, String message) {
		if (o == null) {
			throw message == null ?
			new NullPointerException() : new NullPointerException(message);
		}
	}

	public static void isNull(Object o, String message) {
		if (o != null) {
			throwIllegalArgument(message);
		}
	}

    public static void notEmpty(String s) {
        Assert.notNull(s);
        if(s.trim().length() == 0) {
            throwIllegalArgument(null);
        }
    }

	public static void isTrue(boolean b) {
		isTrue(b, null);
	}

	public static void isTrue(boolean b, String message) {
		if (!b) {
			throwIllegalArgument(message);
		}
	}

	public static void notEmpty(Collection<?> c, String message) {
		notNull(c, message);
		if (c.isEmpty()) {
			throwIllegalArgument(message);
		}
	}

	public static void notEmpty(Object[] a, String message) {
		notNull(a, message);
		if (a.length == 0) {
			throwIllegalArgument(message);
		}
	}

	public static void hasText(String s) {
		hasText(s, null);
	}

	public static void hasText(String s, String message) {
		if (s == null || s.length() == 0) {
			throwIllegalArgument(message);
		}
	}

	private static void throwIllegalArgument(String s) {
		throw s == null ?
				new IllegalArgumentException() : new IllegalArgumentException(s);
	}
}