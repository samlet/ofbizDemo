<Items name="ofbizDemoList">
  <#if ofbizDemoList?has_content>
        <#list ofbizDemoList as ofbizDemo>
        <Item>
            <ofbizDemoId>${ofbizDemo.ofbizDemoId}</ofbizDemoId>
            <ofbizDemoType>${ofbizDemo.getRelatedOne("OfbizDemoType").get("description", locale)}</ofbizDemoType>
            <firstName>${ofbizDemo.firstName?default("NA")}</firstName>
            <lastName>${ofbizDemo.lastName?default("NA")}</lastName>
            <comments>${ofbizDemo.comments!}</comments>
        </Item>
        </#list>
  </#if>
</Items>

