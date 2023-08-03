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
                <li>.Net SDK</li>
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

                                    Create a getting started project with:

                                    <pre class="language-bash container" data-prismjs-copy="Copy">
                <code class="language-bash">
dotnet new console -n getting-started-csharp-client-api
                </code>
            </pre>

                                    Change to the folder of your project and add the MigratoryData library:

                                    <pre class="language-bash container" data-prismjs-copy="Copy">
                <code class="language-bash">
dotnet add package MigratoryData.Client.DotNet --version 6.0.1
                </code>
            </pre>

                                    Copy the below code snippet to the file <code>Program.cs</code> of your project:

                                            <pre class="language-csharp" data-prismjs-copy="Copy">
                <code class="language-csharp container">
// For API documentation go to [Developer&rsquo;s Guide](https://migratorydata.com/docs/client-api/ios/)
using System;
using System.Collections.Generic;
using com.migratorydata.client;
using System.Threading;
namespace example
{
    class Program
    {
        static void Main(string[] args)
        {
            MigratoryDataClient client = new MigratoryDataClient();
            client.SetLogListener(new LogList(), MigratoryDataLogLevel.DEBUG);
            client.SetListener(new Listener());

            client.SetEncryption(true);
            client.SetEntitlementToken(&quot;{{ token }}&quot;);
            client.SetServers(new string[] { &quot;{{ server }}&quot; });

            List&lt;string&gt; subjects = new List&lt;string&gt;();
            subjects.Add(&quot;{{ endpoint.endpoint }}&quot;);
            client.Subscribe(subjects);

            client.Connect();

            // publish a message every 3 seconds
            int count = 1;
            while (count &lt; 1000000)
            {
                client.Publish(new MigratoryDataMessage(&quot;{{ endpoint.endpoint }}&quot;, System.Text.Encoding.ASCII.GetBytes(&quot;data - &quot; + count), &quot;id&quot; + count));
                count++;
                Thread.Sleep(3000);
            }
        }
        class Listener : MigratoryDataListener
        {
            public void OnMessage(MigratoryDataMessage message)
            {
                System.Console.WriteLine(message.ToString());
            }
            public void OnStatus(string status, string info)
            {
                System.Console.WriteLine(status + &quot; &quot; + info);
            }
        }
        class LogList : MigratoryDataLogListener
        {
            public void OnLog(string log, MigratoryDataLogLevel level)
            {
                string msg = string.Format(&quot;[{0:G}] [{1}] {2}&quot;, DateTime.Now, level, log);
                Console.WriteLine(msg);
            }
        }
    }
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
                                Build and run the project as follows:
                                    <pre class="language-bash container" style="min-width:330px;" data-prismjs-copy="Copy">
                <code class="language-bash">
dotnet run
                </code>
            </pre>

                            </div>
                        </div>


</div>
</template>

