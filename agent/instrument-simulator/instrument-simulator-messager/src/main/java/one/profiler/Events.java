/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package one.profiler;

/**
 * Predefined event names to use in {@link AsyncProfiler#start(String, long)}
 */
public class Events {
    public static final String CPU    = "cpu";
    public static final String ALLOC  = "alloc";
    public static final String LOCK   = "lock";
    public static final String WALL   = "wall";
    public static final String ITIMER = "itimer";
}
