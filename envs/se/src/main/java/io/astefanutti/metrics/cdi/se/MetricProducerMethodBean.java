/**
 * Copyright (C) 2013 Antonin Stefanutti (antonin.stefanutti@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.astefanutti.metrics.cdi.se;


import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.RatioGauge;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Metric;
import com.codahale.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class MetricProducerMethodBean {

    @Inject
    private Meter hits;

    @Timed(name = "calls")
    public void cachedMethod(boolean hit) {
        if (hit)
            hits.mark();
    }

    @Produces
    @Metric(name = "cache-hits")
    Gauge<Double> cacheHitRatioGauge(final @Metric(name = "hits") Meter hits,
                                     final @Metric(name = "calls") Timer calls) {
        return new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(hits.getCount(), calls.getCount());
            }
        };
    }
}
