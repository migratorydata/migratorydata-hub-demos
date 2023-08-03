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
                <li>php</li>
                <li>composer</li>
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

        Create a folder for your getting started project. Change to your project folder and add the
                                    MigratoryData library as follows:

                                    <pre class="language-bash container" data-prismjs-copy="Copy">
                <code class="language-bash">
composer require migratorydata/migratorydata-client-reactphp:6.*
                </code>
            </pre>

                                    Copy the below code snippet to a file <code>sample-client.php</code> in your project folder:


                                            <pre class="language-php" data-prismjs-copy="Copy">
                <code class="language-php container">
&lt;?php
// For API documentation go to [Developer&rsquo;s Guide](https://migratorydata.com/docs/client-api/reactphp/)
require __DIR__ . '/vendor/autoload.php';
use MigratoryData\Client\MigratoryDataClient;
use MigratoryData\Client\MigratoryDataException;
use MigratoryData\Client\MigratoryDataMessage;
use MigratoryData\Client\MigratoryDataListener;
class MyListener implements MigratoryDataListener
{
    public function onMessage($message)
    {
        echo &quot;Got message: &quot; . $message . &quot;\\n&quot;;
    }
    public function onStatus($status, $info)
    {
        echo &quot;Got status: &quot; . $status . &quot; - &quot; . $info . &quot;\\n&quot;;
    }
}
$loop = \React\EventLoop\Factory::create();
$client = new MigratoryDataClient();
$client-&gt;setEntitlementToken(&quot;{{ token }}&quot;);
$client-&gt;setLoop($loop);
$client-&gt;setListener(new MyListener());
try {
    $client-&gt;setServers(array(&quot;https://{{ server }}&quot;));
} catch (MigratoryDataException $e) {
    echo($e-&gt;getDetail());
    exit(1);
}
$client-&gt;subscribe(array(&quot;{{ endpoint.endpoint }}&quot;));
$client-&gt;connect();
$loop-&gt;addPeriodicTimer(3, function () use ($client) {
    try {
        $client-&gt;publish(new MigratoryDataMessage(&quot;{{ endpoint.endpoint }}&quot;, &quot;Msg &quot; . time(), &quot;closure-&quot; . time()));
    } catch (MigratoryDataException $e) {
        echo($e-&gt;getDetail());
        echo($e-&gt;getCause());
    }
});
$loop-&gt;run();
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
                                Use the following command to run your project:
                                    <pre class="language-bash container" style="min-width:330px;" data-prismjs-copy="Copy">
                <code class="language-bash">
php sample-client.php
                </code>
            </pre>
                            </div>
                        </div>

</div>
</template>
