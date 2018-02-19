/*
 * Copyright (c) 2017 Sphereon B.V. <https://sphereon.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sphereon.libs.authentication.impl.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    /**
     * Implements BundleActivator.start().
     *
     * @param bundleContext - the framework context for the bundle.
     **/

    public void start(BundleContext bundleContext) {
        System.out.println("Sphereon Easy Blockchain Lib activated");
    }


    /**
     * Implements BundleActivator.stop()
     *
     * @param bundleContext - the framework context for the bundle
     **/
    public void stop(BundleContext bundleContext) {
        System.out.println("Sphereon Easy Blockchain Lib deactivated");
    }
}