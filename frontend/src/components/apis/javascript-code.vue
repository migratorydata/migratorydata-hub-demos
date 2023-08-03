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
                <li>Node.js</li>
                <li>npm</li>
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

        Create a getting started project:

<pre class="language-bash container" data-prismjs-copy="Copy">
<code class="language-bash">
npm init --yes
</code>
</pre>

Add the API library with:

<pre class="language-bash container" data-prismjs-copy="Copy">
<code class="language-bash">
npm install migratorydata-client@~6.0.1
</code>
</pre>

Copy the following code snippet to a file <code>sample-client.js</code> in your project:


<pre class="language-javascript" data-prismjs-copy="Copy">
    <code class="language-javascript container">
        // For API documentation go to [Developer&rsquo;s Guide](https://migratorydata.com/docs/client-api/nodejs/)
require(&quot;migratorydata-client&quot;);
function publish() {
    var date = new Date();
    var time = date.getTime();
    var message = {
        subject : &quot;{{ endpoint.endpoint }}&quot;,
        content : &quot;content-&quot; + time,
        closure : &quot;id-&quot; + time
    };
    client.publish(message);
}
var client = new MigratoryDataClient();
// uncomment next line for setups using load balancing with DNS round-robin
//client.setDnsOptions({dnsResolve: true});
client.setMessageHandler(function(message) {
    console.log(&quot;Got message : [&quot; + message.subject + &quot; = &quot; + message.content + &quot;]&quot;);
});
client.setStatusHandler(function(event) {
    console.log(&quot;Status : &quot; + event.type + &quot; : &quot; + event.info);

    // in this example, publication starts once it gets NOTIFY_SERVER_UP
    if (event.type == MigratoryDataClient.NOTIFY_SERVER_UP) {
        publish();
    }

    // normally, next publication should be performed once the client gets
    // the status of the previous publication
    if (event.type ==  MigratoryDataClient.NOTIFY_PUBLISH_OK) {
        // publish a new message after let's say 1000 milliseconds
        setTimeout(function() {
            publish();
        }, 1000);
    }

    if (event.type ==  MigratoryDataClient.NOTIFY_PUBLISH_FAILED) {
    // normally the previous message should be republished here
    }

    if (event.type ==  MigratoryDataClient.NOTIFY_PUBLISH_DENIED) {
        console.log(&quot;Check your entitlement token&quot;);
        }
});
client.setEntitlementToken(&quot;{{ token }}&quot;);
client.setServers([ &quot;https://{{ server }}&quot; ]);
client.subscribe([ &quot;{{ endpoint.endpoint }}&quot; ]);
client.connect();
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
                                Run your project:
                                    <pre class="language-bash container" style="min-width:330px;" data-prismjs-copy="Copy">
                <code class="language-bash">
node sample-client.js
                </code>
            </pre>
                            </div>
                        </div>

</div>
</template>

