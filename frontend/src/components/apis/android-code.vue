<script setup>
import { onMounted } from 'vue';

defineProps(["endpoint", "token", "server"]);

onMounted(() =>  {
    Prism.highlightAll()
});
</script>

<template>
    <div>

        <div class="d-flex mb-1">
    <div class="d-flex flex-column pr-4 align-items-center">
        <i class="bi bi-2-circle fs-3"></i>
        <div class="line h-100"></div>
    </div>
    <div>
        <h5 class="text-dark mx-2 mt-2 mb-4">Prerequisites</h5>
           <ul>
                <li>Android Studio 2021+</li>
                <li>git</li>
            </ul>

    </div>
</div>

<div class="d-flex mb-1">
    <div class="d-flex flex-column pr-4 align-items-center">
        <i class="bi bi-3-circle fs-3"></i>
        <div class="line h-100"></div>
    </div>
    <div>
        <h5 class="text-dark mx-2 mt-2 mb-4">Getting started project</h5>

        Download the getting started project:

<pre class="language-bash container" data-prismjs-copy="Copy">
<code class="language-bash">
git clone https://github.com/migratorydata/getting-started-android-client-api.git
</code>
</pre>

Edit the file <code>com.migratorydata.androidexampleapplication.Config</code> of the project as follows:

<pre class="language-java container" data-prismjs-copy="Copy">
    <code class="language-java">
// For API documentation go to [Developer&rsquo;s Guide](https://migratorydata.com/docs/client-api/android/)
package com.migratorydata.androidexampleapplication;
public class Config {
    public static final boolean encryption = false;
    public static final String[] server = new String[] {&quot;{{ server }}&quot;};
    public static final String subject = &quot;{{ endpoint.endpoint }}&quot;;
    public static final String token = &quot;{{ token }}&quot;;
}
    </code>
</pre>
    </div>
</div>

                        <div class="d-flex mb-1">
                            <div class="d-flex flex-column pr-4 align-items-center">
                                <i class="bi bi-4-circle fs-3"></i>
                                <div class="line h-100 d-none"></div>
                            </div>
                            <div>
                                <h5 class="text-dark mx-2 mt-2 mb-4">Build and run</h5>
                                Compile and run your project using Android Studio.
                            </div>
                        </div>


</div>
</template>

