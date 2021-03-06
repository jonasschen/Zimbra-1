/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2008, 2009, 2010 Zimbra, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package org.jivesoftware.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized management of caches. Caches are essential for performance and scalability.
 *
 * @see Cache
 * @author Matt Tucker
 */
public class CacheManager {

    private static Map<String, Cache> caches = new HashMap<String, Cache>();
    private static final long DEFAULT_EXPIRATION_TIME = JiveConstants.HOUR * 6;

    /**
     * Initializes a cache given it's name and max size. The default expiration time
     * of six hours will be used. If a cache with the same name has already been initialized,
     * this method returns the existing cache.<p>
     *
     * The size and expiration time for the cache can be overridden by setting Jive properties
     * in the format:<ul>
     *
     *  <li>Size: "cache.CACHE_NAME.size", in bytes.
     *  <li>Expiration: "cache.CACHE_NAME.expirationTime", in milleseconds.
     * </ul>
     * where CACHE_NAME is the name of the cache.
     *
     * @param name the name of the cache to initialize.
     * @param propertiesName  the properties file name prefix where settings for the cache
     *                        are stored. The name is will be prefixed by "cache." before it is
     *                        looked up.
     * @param size the size the cache can grow to, in bytes.
     */
    public static <K,V> Cache<K,V> initializeCache(String name, String propertiesName, int size) {
        return initializeCache(name, propertiesName, size, DEFAULT_EXPIRATION_TIME);
    }

    /**
     * Initializes a cache given it's name, max size, and expiration time. If a cache with
     * the same name has already been initialized, this method returns the existing cache.<p>
     *
     * The size and expiration time for the cache can be overridden by setting Jive properties
     * in the format:<ul>
     *
     *  <li>Size: "cache.CACHE_NAME.size", in bytes.
     *  <li>Expiration: "cache.CACHE_NAME.expirationTime", in milleseconds.
     * </ul>
     * where CACHE_NAME is the name of the cache.
     *
     * @param name the name of the cache to initialize.
     * @param propertiesName  the properties file name prefix where settings for the cache are
     *                        stored. The name is will be prefixed by "cache." before it is
     *                        looked up.
     * @param size the size  the cache can grow to, in bytes.
     * @param expirationTime the default max lifetime of the cache, in milliseconds.
     */
    public static <K,V> Cache<K,V> initializeCache(String name, String propertiesName, int size,
            long expirationTime) {
        Cache<K,V> cache = caches.get(name);
        if (cache == null) {
            size = IMConfig.getCacheSize(propertiesName, size);
            expirationTime = IMConfig.getCacheExpirationTime(propertiesName, (int)expirationTime);
            
            cache = new Cache<K,V>(name, size, expirationTime);
            caches.put(name, cache);
        }
        return cache;
    }

    /**
     * Returns the cache specified by name. The cache must be initialized before this
     * method can be called.
     *
     * @param name the name of the cache to return.
     * @return the cache found, or <tt>null</tt> if no cache by that name
     *      has been initialized.
     */
    public static <K,V> Cache<K,V> getCache(String name) {
        return caches.get(name);
    }

    /**
     * Returns the list of caches being managed by this manager.
     *
     * @return the list of caches being managed by this manager.
     */
    public static Collection<Cache> getCaches() {
        return caches.values();
    }
}