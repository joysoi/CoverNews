package com.nikola.covernews.internal

import kotlinx.coroutines.*

/*
Optionally, async can be made lazy by setting its
start parameter to CoroutineStart.LAZY. In this mode it only starts the coroutine when its result is required by await,
or if its Job's start function is invoked
 */

/*
This will let us set up a unit of async work ahead of time, but not actually invoke it until we interact with its result later on.

Here 2 things are done:
1. Deferred(the Method) is only executed when it has to, by setting the Coroutine property to Lazy.
2. Wrap the object (block) in a lazy initializer itself.

 */
//1. Deferred(the Method) is only executed when it has to, by setting the Coroutine property to Lazy.
fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
   // 2. Wrap the object (block) in a lazy initializer itself.
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}


/*
Lazy
lazy() is a function that takes a lambda and returns an instance of Lazy<T> which can serve as a delegate for implementing a lazy property:
the first call to get() executes the lambda passed to lazy() and remembers the result, subsequent calls to get() simply return the remembered result.
 */