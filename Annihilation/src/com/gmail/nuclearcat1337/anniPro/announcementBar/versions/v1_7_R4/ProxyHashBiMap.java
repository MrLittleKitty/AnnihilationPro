package com.gmail.nuclearcat1337.anniPro.announcementBar.versions.v1_7_R4;

import java.util.HashMap;
import java.util.Set;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.util.com.google.common.collect.BiMap;
import net.minecraft.util.com.google.common.collect.HashBiMap;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class ProxyHashBiMap<K, V> implements BiMap<K, V>
{

    private BiMap<K, V> delegate;

    private ProxyHashBiMap<V, K> inverse;

    // This will proxy
    protected Map<K, K> proxy = new HashMap<K, K>(1);

    public ProxyHashBiMap(BiMap<K, V> map)
    {
        this.delegate = map;
        this.inverse = new Inverse(this, map.inverse(), map);
    }

    private ProxyHashBiMap(BiMap<K, V> map, BiMap<V, K> inversed)
    {
        this.delegate = map;
        this.inverse = null;
    }

    @Override
    public int size()
    {
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return this.delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return this.delegate.containsValue(value);
    }

    @Override
    public V get(Object key)
    {
        Object okey = null;
        if((okey = this.proxy.get(key)) != null)
            key = okey;
        return this.delegate.get(key);
    }

    @Override
    public V put(@Nullable K k, @Nullable V v)
    {
        return this.delegate.put(k, v);
    }

    @Override
    public V remove(Object key)
    {
        return this.delegate.remove(key);
    }

    @Override
    public V forcePut(@Nullable K k, @Nullable V v)
    {
        return this.delegate.forcePut(k, v);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map)
    {
        this.delegate.clear();
    }

    @Override
    public void clear()
    {
        this.delegate.clear();
    }

    @Override
    public Set<K> keySet()
    {
        return this.delegate.keySet();
    }

    @Override
    public Set<V> values()
    {
        return this.delegate.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet()
    {
        return this.delegate.entrySet();
    }

    @Override
    public ProxyHashBiMap<V, K> inverse()
    {
        return this.inverse;
    }

    public void injectSpecial(K key, K okey)
    {
        this.proxy.put(key, okey);
    }

    public void ejectSpecial(K key)
    {
        this.proxy.remove(key);
    }

    private class Inverse<V, K> extends ProxyHashBiMap<V, K>
    {
        private ProxyHashBiMap original;

        private Inverse(ProxyHashBiMap original, BiMap<V, K> forward, BiMap<K, V> backward)
        {
            super(forward, backward);
            this.original = original;
        }

        /**
         * Prevent creation of more Maps, just return the original
         * @return the original BiMap
         */
        @Override
        public ProxyHashBiMap<K, V> inverse()
        {
            return this.original;
        }
    }
}
