/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.celeborn.common.quota

import org.junit.Assert.assertEquals

import org.apache.celeborn.common.RssConf
import org.apache.celeborn.common.protocol.message.ControlMessages.UserIdentifier

class DefaultQuotaManagerSuite extends BaseQuotaManagerSuite {

  override def beforeAll(): Unit = {
    val conf = new RssConf()
    conf.set("rss.quota.configuration.path", getTestResourceFile("test-quota.yaml").getPath)
    quotaManager = QuotaManager.instantiate(conf)
  }

  test("initialize QuotaManager") {
    assert(quotaManager.isInstanceOf[DefaultQuotaManager])
  }

  test("test rss quota conf") {
    assertEquals(
      quotaManager.getQuota(UserIdentifier("AAA", "Tom")),
      Quota(10000, 200, -1, -1))
    assertEquals(
      quotaManager.getQuota(UserIdentifier("BBB", "Jerry")),
      Quota(-1, -1, 10000, 200))
  }
}
