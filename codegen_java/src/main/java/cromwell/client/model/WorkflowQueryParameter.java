/*
 * Cromwell Server REST API
 * Describes the REST API provided by a Cromwell server
 *
 * The version of the OpenAPI document: 30
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package cromwell.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cromwell.client.JSON;

/**
 * Workflow query parameters
 */
@ApiModel(description = "Workflow query parameters")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-10-27T16:16:35.079925Z[Etc/UTC]")
public class WorkflowQueryParameter {
  public static final String SERIALIZED_NAME_SUBMISSION = "submission";
  @SerializedName(SERIALIZED_NAME_SUBMISSION)
  private OffsetDateTime submission;

  public static final String SERIALIZED_NAME_START = "start";
  @SerializedName(SERIALIZED_NAME_START)
  private OffsetDateTime start;

  public static final String SERIALIZED_NAME_END = "end";
  @SerializedName(SERIALIZED_NAME_END)
  private OffsetDateTime end;

  /**
   * Returns only workflows with the specified status.  If specified multiple times, returns workflows in any of the specified statuses. 
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    SUBMITTED("Submitted"),
    
    RUNNING("Running"),
    
    ABORTING("Aborting"),
    
    FAILED("Failed"),
    
    SUCCEEDED("Succeeded"),
    
    ABORTED("Aborted");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<StatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StatusEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public StatusEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return StatusEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  private StatusEnum status;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private String id;

  public static final String SERIALIZED_NAME_EXCLUDE_LABEL_AND = "excludeLabelAnd";
  @SerializedName(SERIALIZED_NAME_EXCLUDE_LABEL_AND)
  private List excludeLabelAnd;

  public static final String SERIALIZED_NAME_EXCLUDE_LABEL_OR = "excludeLabelOr";
  @SerializedName(SERIALIZED_NAME_EXCLUDE_LABEL_OR)
  private List excludeLabelOr;

  public static final String SERIALIZED_NAME_INCLUDE_SUBWORKFLOWS = "includeSubworkflows";
  @SerializedName(SERIALIZED_NAME_INCLUDE_SUBWORKFLOWS)
  private Boolean includeSubworkflows;

  public static final String SERIALIZED_NAME_PAGE = "page";
  @SerializedName(SERIALIZED_NAME_PAGE)
  private Integer page;

  public static final String SERIALIZED_NAME_PAGE_SIZE = "pageSize";
  @SerializedName(SERIALIZED_NAME_PAGE_SIZE)
  private Integer pageSize;

  public WorkflowQueryParameter() {
  }

  public WorkflowQueryParameter submission(OffsetDateTime submission) {
    
    this.submission = submission;
    return this;
  }

   /**
   * Returns only workflows with an equal or later submission time. Can be specified at most once. If both submission time and start date are specified, submission time should be before or equal to start date. 
   * @return submission
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Returns only workflows with an equal or later submission time. Can be specified at most once. If both submission time and start date are specified, submission time should be before or equal to start date. ")

  public OffsetDateTime getSubmission() {
    return submission;
  }


  public void setSubmission(OffsetDateTime submission) {
    this.submission = submission;
  }


  public WorkflowQueryParameter start(OffsetDateTime start) {
    
    this.start = start;
    return this;
  }

   /**
   * Returns only workflows with an equal or later start datetime.  Can be specified at most once. If both start and end date are specified, start date must be before or equal to end date. 
   * @return start
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Returns only workflows with an equal or later start datetime.  Can be specified at most once. If both start and end date are specified, start date must be before or equal to end date. ")

  public OffsetDateTime getStart() {
    return start;
  }


  public void setStart(OffsetDateTime start) {
    this.start = start;
  }


  public WorkflowQueryParameter end(OffsetDateTime end) {
    
    this.end = end;
    return this;
  }

   /**
   * Returns only workflows with an equal or earlier end datetime.  Can be specified at most once. If both start and end date are specified, start date must be before or equal to end date. 
   * @return end
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Returns only workflows with an equal or earlier end datetime.  Can be specified at most once. If both start and end date are specified, start date must be before or equal to end date. ")

  public OffsetDateTime getEnd() {
    return end;
  }


  public void setEnd(OffsetDateTime end) {
    this.end = end;
  }


  public WorkflowQueryParameter status(StatusEnum status) {
    
    this.status = status;
    return this;
  }

   /**
   * Returns only workflows with the specified status.  If specified multiple times, returns workflows in any of the specified statuses. 
   * @return status
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Returns only workflows with the specified status.  If specified multiple times, returns workflows in any of the specified statuses. ")

  public StatusEnum getStatus() {
    return status;
  }


  public void setStatus(StatusEnum status) {
    this.status = status;
  }


  public WorkflowQueryParameter name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * Returns only workflows with the specified name.  If specified multiple times, returns workflows with any of the specified names. 
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Returns only workflows with the specified name.  If specified multiple times, returns workflows with any of the specified names. ")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public WorkflowQueryParameter id(String id) {
    
    this.id = id;
    return this;
  }

   /**
   * Returns only workflows with the specified workflow id.  If specified multiple times, returns workflows with any of the specified workflow ids. 
   * @return id
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Returns only workflows with the specified workflow id.  If specified multiple times, returns workflows with any of the specified workflow ids. ")

  public String getId() {
    return id;
  }


  public void setId(String id) {
    this.id = id;
  }


  public WorkflowQueryParameter excludeLabelAnd(List excludeLabelAnd) {
    
    this.excludeLabelAnd = excludeLabelAnd;
    return this;
  }

   /**
   * Excludes workflows with the specified label.  If specified multiple times, excludes workflows with all of the specified label keys. Specify the label key and label value pair as separated with \&quot;label-key:label-value\&quot; 
   * @return excludeLabelAnd
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Excludes workflows with the specified label.  If specified multiple times, excludes workflows with all of the specified label keys. Specify the label key and label value pair as separated with \"label-key:label-value\" ")

  public List getExcludeLabelAnd() {
    return excludeLabelAnd;
  }


  public void setExcludeLabelAnd(List excludeLabelAnd) {
    this.excludeLabelAnd = excludeLabelAnd;
  }


  public WorkflowQueryParameter excludeLabelOr(List excludeLabelOr) {
    
    this.excludeLabelOr = excludeLabelOr;
    return this;
  }

   /**
   * Excludes workflows with the specified label.  If specified multiple times, excludes workflows with any of the specified label keys. Specify the label key and label value pair as separated with \&quot;label-key:label-value\&quot; 
   * @return excludeLabelOr
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Excludes workflows with the specified label.  If specified multiple times, excludes workflows with any of the specified label keys. Specify the label key and label value pair as separated with \"label-key:label-value\" ")

  public List getExcludeLabelOr() {
    return excludeLabelOr;
  }


  public void setExcludeLabelOr(List excludeLabelOr) {
    this.excludeLabelOr = excludeLabelOr;
  }


  public WorkflowQueryParameter includeSubworkflows(Boolean includeSubworkflows) {
    
    this.includeSubworkflows = includeSubworkflows;
    return this;
  }

   /**
   * Include subworkflows in results. By default, it is taken as true.
   * @return includeSubworkflows
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Include subworkflows in results. By default, it is taken as true.")

  public Boolean getIncludeSubworkflows() {
    return includeSubworkflows;
  }


  public void setIncludeSubworkflows(Boolean includeSubworkflows) {
    this.includeSubworkflows = includeSubworkflows;
  }


  public WorkflowQueryParameter page(Integer page) {
    
    this.page = page;
    return this;
  }

   /**
   * When pageSize is set, which page of results to return. If not set, the first page of &#39;pageSize&#39; results will be returned.
   * @return page
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "When pageSize is set, which page of results to return. If not set, the first page of 'pageSize' results will be returned.")

  public Integer getPage() {
    return page;
  }


  public void setPage(Integer page) {
    this.page = page;
  }


  public WorkflowQueryParameter pageSize(Integer pageSize) {
    
    this.pageSize = pageSize;
    return this;
  }

   /**
   * The number of results to return at a time
   * @return pageSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The number of results to return at a time")

  public Integer getPageSize() {
    return pageSize;
  }


  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * A container for additional, undeclared properties.
   * This is a holder for any undeclared properties as specified with
   * the 'additionalProperties' keyword in the OAS document.
   */
  private Map<String, Object> additionalProperties;

  /**
   * Set the additional (undeclared) property with the specified name and value.
   * If the property does not already exist, create it otherwise replace it.
   */
  public WorkflowQueryParameter putAdditionalProperty(String key, Object value) {
    if (this.additionalProperties == null) {
        this.additionalProperties = new HashMap<String, Object>();
    }
    this.additionalProperties.put(key, value);
    return this;
  }

  /**
   * Return the additional (undeclared) property.
   */
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  /**
   * Return the additional (undeclared) property with the specified name.
   */
  public Object getAdditionalProperty(String key) {
    if (this.additionalProperties == null) {
        return null;
    }
    return this.additionalProperties.get(key);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorkflowQueryParameter workflowQueryParameter = (WorkflowQueryParameter) o;
    return Objects.equals(this.submission, workflowQueryParameter.submission) &&
        Objects.equals(this.start, workflowQueryParameter.start) &&
        Objects.equals(this.end, workflowQueryParameter.end) &&
        Objects.equals(this.status, workflowQueryParameter.status) &&
        Objects.equals(this.name, workflowQueryParameter.name) &&
        Objects.equals(this.id, workflowQueryParameter.id) &&
        Objects.equals(this.excludeLabelAnd, workflowQueryParameter.excludeLabelAnd) &&
        Objects.equals(this.excludeLabelOr, workflowQueryParameter.excludeLabelOr) &&
        Objects.equals(this.includeSubworkflows, workflowQueryParameter.includeSubworkflows) &&
        Objects.equals(this.page, workflowQueryParameter.page) &&
        Objects.equals(this.pageSize, workflowQueryParameter.pageSize)&&
        Objects.equals(this.additionalProperties, workflowQueryParameter.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(submission, start, end, status, name, id, excludeLabelAnd, excludeLabelOr, includeSubworkflows, page, pageSize, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorkflowQueryParameter {\n");
    sb.append("    submission: ").append(toIndentedString(submission)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    excludeLabelAnd: ").append(toIndentedString(excludeLabelAnd)).append("\n");
    sb.append("    excludeLabelOr: ").append(toIndentedString(excludeLabelOr)).append("\n");
    sb.append("    includeSubworkflows: ").append(toIndentedString(includeSubworkflows)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static HashSet<String> openapiFields;
  public static HashSet<String> openapiRequiredFields;

  static {
    // a set of all properties/fields (JSON key names)
    openapiFields = new HashSet<String>();
    openapiFields.add("submission");
    openapiFields.add("start");
    openapiFields.add("end");
    openapiFields.add("status");
    openapiFields.add("name");
    openapiFields.add("id");
    openapiFields.add("excludeLabelAnd");
    openapiFields.add("excludeLabelOr");
    openapiFields.add("includeSubworkflows");
    openapiFields.add("page");
    openapiFields.add("pageSize");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to WorkflowQueryParameter
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (WorkflowQueryParameter.openapiRequiredFields.isEmpty()) {
          return;
        } else { // has required fields
          throw new IllegalArgumentException(String.format("The required field(s) %s in WorkflowQueryParameter is not found in the empty JSON string", WorkflowQueryParameter.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("status") != null && !jsonObj.get("status").isJsonNull()) && !jsonObj.get("status").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("status").toString()));
      }
      if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      if ((jsonObj.get("id") != null && !jsonObj.get("id").isJsonNull()) && !jsonObj.get("id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("id").toString()));
      }
      if ((jsonObj.get("excludeLabelAnd") != null && !jsonObj.get("excludeLabelAnd").isJsonNull()) && !jsonObj.get("excludeLabelAnd").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `excludeLabelAnd` to be a primitive type in the JSON string but got `%s`", jsonObj.get("excludeLabelAnd").toString()));
      }
      if ((jsonObj.get("excludeLabelOr") != null && !jsonObj.get("excludeLabelOr").isJsonNull()) && !jsonObj.get("excludeLabelOr").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `excludeLabelOr` to be a primitive type in the JSON string but got `%s`", jsonObj.get("excludeLabelOr").toString()));
      }
      if ((jsonObj.get("includeSubworkflows") != null && !jsonObj.get("includeSubworkflows").isJsonNull()) && !jsonObj.get("includeSubworkflows").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `includeSubworkflows` to be a primitive type in the JSON string but got `%s`", jsonObj.get("includeSubworkflows").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!WorkflowQueryParameter.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'WorkflowQueryParameter' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<WorkflowQueryParameter> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(WorkflowQueryParameter.class));

       return (TypeAdapter<T>) new TypeAdapter<WorkflowQueryParameter>() {
           @Override
           public void write(JsonWriter out, WorkflowQueryParameter value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             obj.remove("additionalProperties");
             // serialize additonal properties
             if (value.getAdditionalProperties() != null) {
               for (Map.Entry<String, Object> entry : value.getAdditionalProperties().entrySet()) {
                 if (entry.getValue() instanceof String)
                   obj.addProperty(entry.getKey(), (String) entry.getValue());
                 else if (entry.getValue() instanceof Number)
                   obj.addProperty(entry.getKey(), (Number) entry.getValue());
                 else if (entry.getValue() instanceof Boolean)
                   obj.addProperty(entry.getKey(), (Boolean) entry.getValue());
                 else if (entry.getValue() instanceof Character)
                   obj.addProperty(entry.getKey(), (Character) entry.getValue());
                 else {
                   obj.add(entry.getKey(), gson.toJsonTree(entry.getValue()).getAsJsonObject());
                 }
               }
             }
             elementAdapter.write(out, obj);
           }

           @Override
           public WorkflowQueryParameter read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             WorkflowQueryParameter instance = thisAdapter.fromJsonTree(jsonObj);
             for (Map.Entry<String, JsonElement> entry : jsonObj.entrySet()) {
               if (!openapiFields.contains(entry.getKey())) {
                 if (entry.getValue().isJsonPrimitive()) { // primitive type
                   if (entry.getValue().getAsJsonPrimitive().isString())
                     instance.putAdditionalProperty(entry.getKey(), entry.getValue().getAsString());
                   else if (entry.getValue().getAsJsonPrimitive().isNumber())
                     instance.putAdditionalProperty(entry.getKey(), entry.getValue().getAsNumber());
                   else if (entry.getValue().getAsJsonPrimitive().isBoolean())
                     instance.putAdditionalProperty(entry.getKey(), entry.getValue().getAsBoolean());
                   else
                     throw new IllegalArgumentException(String.format("The field `%s` has unknown primitive type. Value: %s", entry.getKey(), entry.getValue().toString()));
                 } else { // non-primitive type
                   instance.putAdditionalProperty(entry.getKey(), gson.fromJson(entry.getValue(), HashMap.class));
                 }
               }
             }
             return instance;
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of WorkflowQueryParameter given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of WorkflowQueryParameter
  * @throws IOException if the JSON string is invalid with respect to WorkflowQueryParameter
  */
  public static WorkflowQueryParameter fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, WorkflowQueryParameter.class);
  }

 /**
  * Convert an instance of WorkflowQueryParameter to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

