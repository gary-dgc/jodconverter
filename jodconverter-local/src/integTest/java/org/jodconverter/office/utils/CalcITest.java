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

package org.jodconverter.office.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import org.jodconverter.AbstractOfficeITest;
import org.jodconverter.LocalConverter;
import org.jodconverter.filter.Filter;

public class CalcITest extends AbstractOfficeITest {

  @ClassRule public static TemporaryFolder testFolder = new TemporaryFolder();

  @Test
  public void isCalcAndGetCalcDoc_WithCalcDocument_NoExceptionThrown() {

    final Filter filter =
        (context, document, chain) -> {
          assertThat(Calc.isCalc(document)).isTrue();
          assertThat(Calc.getCalcDoc(null)).isNull();
          assertThat(Calc.getCalcDoc(document)).isNotNull();
        };

    final File outputFile = new File(testFolder.getRoot(), "out.pdf");
    FileUtils.deleteQuietly(outputFile);

    assertThatCode(
            () ->
                LocalConverter.builder()
                    .filterChain(filter)
                    .build()
                    .convert(new File(DOCUMENTS_DIR + "test.ods"))
                    .to(outputFile)
                    .execute())
        .doesNotThrowAnyException();
  }

  @Test
  public void isNotCalcAndGetCalcDoc_WithTextDocument_NoExceptionThrown() {

    final Filter filter =
        (context, document, chain) -> {
          assertThat(Calc.isCalc(document)).isFalse();
          assertThat(Calc.getCalcDoc(null)).isNull();
          assertThat(Calc.getCalcDoc(document)).isNull();
        };

    final File outputFile = new File(testFolder.getRoot(), "out.pdf");
    FileUtils.deleteQuietly(outputFile);

    assertThatCode(
            () ->
                LocalConverter.builder()
                    .filterChain(filter)
                    .build()
                    .convert(new File(DOCUMENTS_DIR + "test.odt"))
                    .to(outputFile)
                    .execute())
        .doesNotThrowAnyException();
  }
}
