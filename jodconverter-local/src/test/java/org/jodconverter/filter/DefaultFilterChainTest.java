/*
 * Copyright 2004 - 2012 Mirko Nasato and contributors
 *           2016 - 2020 Simon Braconnier and contributors
 *
 * This file is part of JODConverter - Java OpenDocument Converter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jodconverter.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Test;

import org.jodconverter.office.OfficeException;

/**
 * Contains tests for the {@link org.jodconverter.filter.DefaultFilterChain} class.
 *
 * @see org.jodconverter.filter.DefaultFilterChain
 */
@SuppressWarnings("unchecked")
public class DefaultFilterChainTest {

  /** Tests that a DefaultFilterChain is created empty by default. */
  @Test
  public void create_WithoutFilters_ShouldBeEmpty() throws IllegalAccessException {

    final DefaultFilterChain chain = new DefaultFilterChain();

    final List<Filter> filters = (List<Filter>) FieldUtils.readField(chain, "filters", true);
    assertThat(filters).hasSize(0);
  }

  /** Tests that a DefaultFilterChain.addFilter works as expected. */
  @Test
  public void create_ShouldBeEditable() throws IllegalAccessException {

    final Filter filter = new RefreshFilter();
    final DefaultFilterChain chain = new DefaultFilterChain();
    chain.addFilter(filter);

    final List<Filter> filters = (List<Filter>) FieldUtils.readField(chain, "filters", true);
    assertThat(filters).hasSize(1);
    assertThat(filters).containsExactly(filter);
  }

  @Test
  public void doFilter_WithFilterThrowingException_ThrowsOfficeException() {

    assertThatExceptionOfType(OfficeException.class)
        .isThrownBy(
            () ->
                new DefaultFilterChain(
                        (context, document, chain) -> {
                          throw new OfficeException("Unsupported Filter");
                        })
                    .doFilter(null, null))
        .withNoCause()
        .withMessage("Unsupported Filter");

    assertThatExceptionOfType(OfficeException.class)
        .isThrownBy(
            () ->
                new DefaultFilterChain(
                        (context, document, chain) -> {
                          throw new IndexOutOfBoundsException();
                        })
                    .doFilter(null, null))
        .withCauseExactlyInstanceOf(IndexOutOfBoundsException.class);
  }
}
