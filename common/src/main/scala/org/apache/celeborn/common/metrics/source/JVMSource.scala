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

package org.apache.celeborn.common.metrics.source

import java.lang.management.ManagementFactory

import scala.collection.JavaConverters._

import com.codahale.metrics.Gauge
import com.codahale.metrics.jvm.{BufferPoolMetricSet, GarbageCollectorMetricSet, MemoryUsageGaugeSet}

import org.apache.celeborn.common.RssConf

class JVMSource(rssConf: RssConf, role: String) extends AbstractSource(rssConf, role) {
  override val sourceName = "JVM"

  // all of metrics of GCMetricSet and BufferPoolMetricSet are Gauge
  Seq(
    new GarbageCollectorMetricSet(),
    new MemoryUsageGaugeSet(),
    new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer))
    .map { x =>
      x.getMetrics.asScala.map {
        case (name: String, metric: Gauge[_]) => addGauge(name, metric)
      }
    }
  // start cleaner
  startCleaner()
}
