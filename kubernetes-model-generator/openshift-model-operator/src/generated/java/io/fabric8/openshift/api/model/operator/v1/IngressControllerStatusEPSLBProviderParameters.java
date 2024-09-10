
package io.fabric8.openshift.api.model.operator.v1;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.builder.Editable;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LocalObjectReference;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.BuildableReference;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "aws",
    "gcp",
    "ibm",
    "type"
})
@ToString
@EqualsAndHashCode
@Accessors(prefix = {
    "_",
    ""
})
@Buildable(editableEnabled = false, validationEnabled = false, generateBuilderPackage = false, lazyCollectionInitEnabled = false, builderPackage = "io.fabric8.kubernetes.api.builder", refs = {
    @BuildableReference(ObjectMeta.class),
    @BuildableReference(LabelSelector.class),
    @BuildableReference(Container.class),
    @BuildableReference(PodTemplateSpec.class),
    @BuildableReference(ResourceRequirements.class),
    @BuildableReference(IntOrString.class),
    @BuildableReference(ObjectReference.class),
    @BuildableReference(LocalObjectReference.class),
    @BuildableReference(PersistentVolumeClaim.class)
})
@Generated("jsonschema2pojo")
public class IngressControllerStatusEPSLBProviderParameters implements Editable<IngressControllerStatusEPSLBProviderParametersBuilder> , KubernetesResource
{

    @JsonProperty("aws")
    private IngressControllerStatusEPSLBPPAws aws;
    @JsonProperty("gcp")
    private IngressControllerStatusEPSLBPPGcp gcp;
    @JsonProperty("ibm")
    private IngressControllerStatusEPSLBPPIbm ibm;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public IngressControllerStatusEPSLBProviderParameters() {
    }

    public IngressControllerStatusEPSLBProviderParameters(IngressControllerStatusEPSLBPPAws aws, IngressControllerStatusEPSLBPPGcp gcp, IngressControllerStatusEPSLBPPIbm ibm, String type) {
        super();
        this.aws = aws;
        this.gcp = gcp;
        this.ibm = ibm;
        this.type = type;
    }

    @JsonProperty("aws")
    public IngressControllerStatusEPSLBPPAws getAws() {
        return aws;
    }

    @JsonProperty("aws")
    public void setAws(IngressControllerStatusEPSLBPPAws aws) {
        this.aws = aws;
    }

    @JsonProperty("gcp")
    public IngressControllerStatusEPSLBPPGcp getGcp() {
        return gcp;
    }

    @JsonProperty("gcp")
    public void setGcp(IngressControllerStatusEPSLBPPGcp gcp) {
        this.gcp = gcp;
    }

    @JsonProperty("ibm")
    public IngressControllerStatusEPSLBPPIbm getIbm() {
        return ibm;
    }

    @JsonProperty("ibm")
    public void setIbm(IngressControllerStatusEPSLBPPIbm ibm) {
        this.ibm = ibm;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public IngressControllerStatusEPSLBProviderParametersBuilder edit() {
        return new IngressControllerStatusEPSLBProviderParametersBuilder(this);
    }

    @JsonIgnore
    public IngressControllerStatusEPSLBProviderParametersBuilder toBuilder() {
        return edit();
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

}