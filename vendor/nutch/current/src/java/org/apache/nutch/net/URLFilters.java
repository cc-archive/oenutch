/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nutch.net;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.nutch.plugin.Extension;
import org.apache.nutch.plugin.ExtensionPoint;
import org.apache.nutch.plugin.PluginRuntimeException;
import org.apache.nutch.plugin.PluginRepository;

import org.apache.hadoop.conf.Configuration;
/** Creates and caches {@link URLFilter} implementing plugins.*/
public class URLFilters {

  public static final String URLFILTER_ORDER = "urlfilter.order";
  private URLFilter[] filters;

  public URLFilters(Configuration conf) {
    String order = conf.get(URLFILTER_ORDER);
    this.filters = (URLFilter[]) conf.getObject(URLFilter.class.getName());

    if (this.filters == null) {
      String[] orderedFilters = null;
      if (order != null && !order.trim().equals("")) {
        orderedFilters = order.split("\\s+");
      }

      try {
        ExtensionPoint point = PluginRepository.get(conf).getExtensionPoint(
            URLFilter.X_POINT_ID);
        if (point == null)
          throw new RuntimeException(URLFilter.X_POINT_ID + " not found.");
        Extension[] extensions = point.getExtensions();
        HashMap filterMap = new HashMap();
        for (int i = 0; i < extensions.length; i++) {
          Extension extension = extensions[i];
          URLFilter filter = (URLFilter) extension.getExtensionInstance();
          if (!filterMap.containsKey(filter.getClass().getName())) {
            filterMap.put(filter.getClass().getName(), filter);
          }
        }
        if (orderedFilters == null) {
          conf.setObject(URLFilter.class.getName(), filterMap.values().toArray(
              new URLFilter[0]));
        } else {
          ArrayList filters = new ArrayList();
          for (int i = 0; i < orderedFilters.length; i++) {
            URLFilter filter = (URLFilter) filterMap.get(orderedFilters[i]);
            if (filter != null) {
              filters.add(filter);
            }
          }
          conf.setObject(URLFilter.class.getName(), filters
              .toArray(new URLFilter[filters.size()]));
        }
      } catch (PluginRuntimeException e) {
        throw new RuntimeException(e);
      }
      this.filters = (URLFilter[]) conf.getObject(URLFilter.class.getName());
    }
  }

  /** Run all defined filters. Assume logical AND. */
  public String filter(String urlString) throws URLFilterException {
    for (int i = 0; i < this.filters.length; i++) {
      if (urlString == null)
        return null;
      urlString = this.filters[i].filter(urlString);
    }
    return urlString;
  }
}
