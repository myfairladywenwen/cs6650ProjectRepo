/*
 * Ski Data API for NEU Seattle distributed systems course
 * An API for an emulation of skier managment system for RFID tagged lift tickets. Basis for CS6650 Assignments for 2019
 *
 * OpenAPI spec version: 1.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * ResortsListResorts
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-09-19T20:21:11.675Z[GMT]")
public class ResortsListResorts {
  @SerializedName("resortName")
  private String resortName = null;

  @SerializedName("resortID")
  private Integer resortID = null;

  public ResortsListResorts resortName(String resortName) {
    this.resortName = resortName;
    return this;
  }

   /**
   * Get resortName
   * @return resortName
  **/
  @Schema(description = "")
  public String getResortName() {
    return resortName;
  }

  public void setResortName(String resortName) {
    this.resortName = resortName;
  }

  public ResortsListResorts resortID(Integer resortID) {
    this.resortID = resortID;
    return this;
  }

   /**
   * Get resortID
   * @return resortID
  **/
  @Schema(description = "")
  public Integer getResortID() {
    return resortID;
  }

  public void setResortID(Integer resortID) {
    this.resortID = resortID;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResortsListResorts resortsListResorts = (ResortsListResorts) o;
    return Objects.equals(this.resortName, resortsListResorts.resortName) &&
        Objects.equals(this.resortID, resortsListResorts.resortID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resortName, resortID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResortsListResorts {\n");
    
    sb.append("    resortName: ").append(toIndentedString(resortName)).append("\n");
    sb.append("    resortID: ").append(toIndentedString(resortID)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
