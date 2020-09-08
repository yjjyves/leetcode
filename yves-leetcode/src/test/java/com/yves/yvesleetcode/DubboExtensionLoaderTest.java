package com.yves.yvesleetcode;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.cluster.LoadBalance;
import org.junit.Test;

public class DubboExtensionLoaderTest {
    @Test
    public void test_getExtensionAdaptive() {
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
        System.err.println("==========" + protocol);
    }

    @Test
    public void test_LoadBalance_getExtensionAdaptive() {
        LoadBalance loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getAdaptiveExtension();
        System.err.println("==========" + loadBalance);
    }
}
