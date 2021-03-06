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

package br.com.caelum.vraptor.ioc.pico;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;

public class PicoBasedContainerTest {

	public static class Fruit {

	}

	public static class Juice {
	private final Fruit fruit;

	public Juice(Fruit f) {
		this.fruit = f;
	}
	}

	private PicoBasedContainer container;
	private MutablePicoContainer picoContainer;

	@Before
	public void setup() {
	this.picoContainer = new PicoBuilder().withCaching().build();
	this.container = new PicoBasedContainer(picoContainer);
	}

	@Test
	public void shouldBeAbleToInstantiateABean() {
	this.picoContainer.addComponent(Fruit.class);
	assertThat(container.instanceFor(Fruit.class), is(notNullValue()));
	}

	@Test
	public void shouldBeAbleToProvideAFruitButNotAJuice() {
		this.picoContainer.addComponent(Fruit.class);
		
		assertThat(container.canProvide(Fruit.class), is(true));
		assertThat(container.canProvide(Juice.class), is(false));
	}

	@Test
	public void shouldBeAbleToInstantiateADependentBean() {
	this.picoContainer.addComponent(Fruit.class);
	this.picoContainer.addComponent(Juice.class);
	
	Fruit fruit = container.instanceFor(Fruit.class);
	Juice juice = container.instanceFor(Juice.class);
	
	assertThat(juice, is(notNullValue()));
	assertThat(juice.fruit, is(equalTo(fruit)));
	}
}
