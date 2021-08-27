package com.starter.irpc.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class CuratorClient {
    private CuratorFramework client;

    public CuratorClient(String connectString, String namespace, int sessionTimeout, int connectionTimeout) {
        client = CuratorFrameworkFactory.builder().namespace(namespace).connectString(connectString)
                .sessionTimeoutMs(sessionTimeout).connectionTimeoutMs(connectionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
        client.start();
    }

    public CuratorClient(String connectString, int timeout) {
        this(connectString, ZkConstant.ZK_NAMESPACE, timeout, timeout);
    }

    public CuratorClient(String connectString) {
        this(connectString, ZkConstant.ZK_NAMESPACE, ZkConstant.ZK_SESSION_TIMEOUT, ZkConstant.ZK_CONNECTION_TIMEOUT);
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void addConnectionStateListener(ConnectionStateListener connectionStateListener) {
        client.getConnectionStateListenable().addListener(connectionStateListener);
    }

    public void createPathData(String path, byte[] data) throws Exception {
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, data);
    }

    public boolean containService(String serviceName) throws Exception {
        byte[] bytes = client.getData().forPath(ZkConstant.ZK_REGISTER + "/" + serviceName);
        return bytes.length>0;
    }


    public void updatePathData(String path, byte[] data) throws Exception {
        client.setData().forPath(path, data);
    }

    public void deletePath(String path) throws Exception {
        client.delete().forPath(path);
    }

    public void watchNode(String path, Watcher watcher) throws Exception {
        client.getData().usingWatcher(watcher).forPath(path);
    }

    public byte[] getData(String path) throws Exception {
        return client.getData().forPath(path);
    }


    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    public void watchTreeNode(String path, TreeCacheListener listener) {
        TreeCache treeCache = new TreeCache(client, path);
        treeCache.getListenable().addListener(listener);
    }

    public void watchPathChildrenNode(String path, PathChildrenCacheListener listener) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true);
        //BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        pathChildrenCache.getListenable().addListener(listener);
    }

    public void close() {
        client.close();
    }

    public static void main(String[] args) throws Exception {
        CuratorClient client = new CuratorClient("120.79.220.182:2181");
        boolean helloServiceImpl = client.containService("helloServiceImpl");
        System.out.println(helloServiceImpl);
    }
}
