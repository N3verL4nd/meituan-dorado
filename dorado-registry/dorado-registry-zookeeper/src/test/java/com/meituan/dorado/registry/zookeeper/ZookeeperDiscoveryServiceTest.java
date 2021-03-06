/*
 * Copyright 2018 Meituan Dianping. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meituan.dorado.registry.zookeeper;

import com.meituan.dorado.common.extension.ExtensionLoader;
import com.meituan.dorado.registry.DiscoveryService;
import com.meituan.dorado.registry.RegistryFactory;
import com.meituan.dorado.registry.RegistryService;
import com.meituan.dorado.registry.meta.Provider;
import com.meituan.dorado.registry.meta.RegistryInfo;
import com.meituan.dorado.registry.meta.SubscribeInfo;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class ZookeeperDiscoveryServiceTest extends RegistryTest {

    private static RegistryService registryService;
    private static DiscoveryService discoveryService;

    @BeforeClass
    public static void init() throws Exception {
        initZkServer();
        RegistryFactory factory = ExtensionLoader.getExtensionWithName(RegistryFactory.class, "zookeeper");
        registryService = factory.getRegistryService(address);
        discoveryService = factory.getDiscoveryService(address);
    }

    @AfterClass
    public static void stop() throws Exception {
        discoveryService.destroy();
        registryService.destroy();
        zkServer.stop();
    }

    @Test
    public void testRegisterDiscovery() throws InterruptedException {
        RegistryInfo registryInfo = genRegistryInfo();
        SubscribeInfo subscribeInfo = genSubscribeInfo();
        MockNotifyListener listener = new MockNotifyListener();

        // ??????
        registryService.register(registryInfo);
        Thread.sleep(100);
        // ????????????????????????
        discoveryService.subscribe(subscribeInfo, listener);
        List<Provider> providers = listener.getProviders();
        Assert.assertEquals(1, providers.size());
        Assert.assertEquals(9001, providers.get(0).getPort());

        // ??????
        registryService.unregister(registryInfo);
        Thread.sleep(100);
        // ????????????????????????
        providers = listener.getProviders();
        Assert.assertEquals(0, providers.size());
        // ????????????
        discoveryService.unsubscribe(subscribeInfo);
    }

    @Test
    public void testUnsubscribe() throws InterruptedException {
        RegistryInfo registryInfo = genRegistryInfo();
        SubscribeInfo subscribeInfo = genSubscribeInfo();
        discoveryService.unsubscribe(subscribeInfo);
        MockNotifyListener listener = new MockNotifyListener();

        // ??????
        registryService.register(registryInfo);
        // ????????????????????????
        discoveryService.subscribe(subscribeInfo, listener);
        List<Provider> providers = listener.getProviders();
        Assert.assertEquals(1, providers.size());
        Assert.assertEquals(9001, providers.get(0).getPort());

        // ????????????
        discoveryService.unsubscribe(subscribeInfo);
        // ??????
        registryService.unregister(registryInfo);
        Thread.sleep(100);
        // ??????????????? ????????????????????????
        providers = listener.getProviders();
        Assert.assertEquals(1, providers.size());
        Assert.assertEquals(9001, providers.get(0).getPort());
    }
}
