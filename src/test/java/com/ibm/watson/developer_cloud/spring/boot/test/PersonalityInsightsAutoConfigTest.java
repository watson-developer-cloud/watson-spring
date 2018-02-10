/*
 * Copyright 2018 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.ibm.watson.developer_cloud.spring.boot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.spring.boot.WatsonAutoConfiguration;

import okhttp3.Credentials;

/**
 * The Class PersonalityInsightsAutoConfigTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WatsonAutoConfiguration.class}, loader = AnnotationConfigContextLoader.class)
@TestPropertySource(properties = {
    "watson.personality-insights.url=" + PersonalityInsightsAutoConfigTest.url,
    "watson.personality-insights.username=" + PersonalityInsightsAutoConfigTest.username,
    "watson.personality-insights.password=" + PersonalityInsightsAutoConfigTest.password,
    "watson.personality-insights.versionDate=" + PersonalityInsightsAutoConfigTest.versionDate
})
public class PersonalityInsightsAutoConfigTest {

  static final String url = "http://watson.com/personality-insights";
  static final String username = "sam";

  /** The Constant versionDate. */
  static final String versionDate = "2017-12-15";

  @Autowired
  private ApplicationContext applicationContext;

  /**
   * Personality insights bean config.
   */
  @Test
  public void personalityInsightsBeanConfig() {
    PersonalityInsights personalityInsights = (PersonalityInsights) applicationContext.getBean("personalityInsights");

    assertNotNull(personalityInsights);
    assertEquals(url, personalityInsights.getEndPoint());

    // Verify the credentials and versionDate -- which are stored in private member
    // variables
    try {
      Field apiKeyField = WatsonService.class.getDeclaredField("apiKey");
      apiKeyField.setAccessible(true);
      assertEquals(Credentials.basic(username, password), apiKeyField.get(personalityInsights));

      Field versionField = PersonalityInsights.class.getDeclaredField("versionDate");
      versionField.setAccessible(true);
      assertEquals(versionDate, versionField.get(personalityInsights));
    } catch (NoSuchFieldException | IllegalAccessException ex) {
      // This shouldn't happen
      assert false;
    }
  }
}
