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
                <li>Xcode</li>
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
https://github.com/migratorydata/getting-started-ios-client-api.git
</code>
</pre>

Copy the code snippet below to the file <code>sample-client/Config.m</code> of your project:

        <pre class="language-objc container" data-prismjs-copy="Copy">
<code class="language-objc">
// For API documentation go to [Developer&rsquo;s Guide](https://migratorydata.com/docs/client-api/ios/)
#import &lt;Foundation/Foundation.h&gt;
BOOL ENCRYPTION = YES;
NSString *TOKEN = @&quot;{{ token }}&quot;;
NSString *SERVER = @&quot;{{ server }}&quot;;
NSString *SUBJECT = @&quot;{{ endpoint.endpoint }}&quot;;
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
                                Compile and run your project using Xcode.
                            </div>
                        </div>

</div>
</template>


