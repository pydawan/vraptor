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

import java.util.Deque;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;

/**
 * Default implmentation of a interceptor stack.
 * @author guilherme silveira
 *
 */
@PrototypeScoped
public class DefaultInterceptorStack implements InterceptorStack {

	private static final Logger logger = LoggerFactory.getLogger(DefaultInterceptorStack.class);

    private final Deque<InterceptorHandler> interceptors = new LinkedList<InterceptorHandler>();
    private final Container container;

    public DefaultInterceptorStack(Container container) {
        this.container = container;
    }

    public void add(Interceptor interceptor) {
        this.interceptors.addLast(new InstantiatedInterceptorHandler(interceptor));
    }

    public void next(ResourceMethod method, Object resourceInstance) throws InterceptionException {
        if (interceptors.isEmpty()) {
        	logger.debug("All registered interceptors have being called. End of VRaptor Request Execution.");
            return;
        }
        InterceptorHandler handler = interceptors.pollFirst();
        handler.execute(this, method, resourceInstance);
    }

    public <T extends Interceptor> void add(Class<T> type) {
        this.interceptors.addLast(new ToInstantiateInterceptorHandler(container, type));
    }

    public void addAsNext(Interceptor interceptor) {
        this.interceptors.addFirst(new InstantiatedInterceptorHandler(interceptor));
    }

    public void addAsNext(Class<? extends Interceptor> interceptor) {
    	this.interceptors.addFirst(new ToInstantiateInterceptorHandler(container, interceptor));
    }

    @Override
    public String toString() {
    	return "DefaultInterceptorStack " + interceptors;
    }

}
