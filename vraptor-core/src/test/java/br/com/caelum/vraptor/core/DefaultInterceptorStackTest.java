/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.caelum.vraptor.core;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;
import org.mockito.InOrder;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.interceptor.Interceptor;

public class DefaultInterceptorStackTest {

    @Test
    public void testInvokesAllInterceptorsInItsCorrectOrder() throws IOException, InterceptionException {
        DefaultInterceptorStack stack = new DefaultInterceptorStack(null);
        Interceptor first = mock(Interceptor.class, "firstMocked");
        Interceptor second = mock(Interceptor.class, "secondMocked");
        stack.add(first);
        stack.add(second);
        stack.next(null, null);

        InOrder order = inOrder(first,second);

        order.verify(first).accepts(null);
        order.verify(second).accepts(null);
    }

    @Test
    public void shouldAddNextInterceptorAsNext() throws InterceptionException, IOException {
        Interceptor first = mock(Interceptor.class, "firstMocked");
        final Interceptor second = mock(Interceptor.class, "secondMocked");
        final DefaultInterceptorStack stack = new DefaultInterceptorStack(null);
        stack.add(first);
        stack.addAsNext(second);

        stack.next(null, null);

        InOrder order = inOrder(first,second);

        order.verify(second).accepts(null);
        order.verify(first).accepts(null);
    }

    @Test
    public void shouldAddInterceptorAsLast() throws InterceptionException, IOException {
        final Interceptor firstMocked = mock(Interceptor.class, "firstMocked");
        final Interceptor secondMocked = mock(Interceptor.class, "secondMocked");
        final DefaultInterceptorStack stack = new DefaultInterceptorStack(null);
        stack.add(firstMocked);
        stack.add(secondMocked);

        when(firstMocked.accepts(null)).thenReturn(true);

        stack.next(null, null);

        verify(firstMocked).intercept(stack, null, null);
    }

}
