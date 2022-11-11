/*
 * Copyright (c) 2017 Sphereon B.V. <https://sphereon.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sphereon.libs.blockchain.api.model;

import com.sphereon.libs.blockchain.api.HasContent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by niels on 18-10-16.
 */

@XmlRootElement
@ApiModel(value = "Entry Data", description = "Entry Data contains the actual content and a list of External IDs (metadata)")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EntryData implements HasContent<byte[]> {
    @XmlElement(name = "externalIds")
    @ApiModelProperty(value = "External Ids", notes = "A list of Exteral Ids. These are metadata objects that can store additional bytes. External Ids are included both in Chain Id and Entry Id calculation", required = false)
    private List<ExternalId> externalIds = new ArrayList<>();

    @XmlElement
    @ApiModelProperty(value = "Content", notes = "Content is anything you'd like to store in this entry, since it is a byte array. Please note that content is included during Entry Id calculation but omitted for Chain Id calculation", required = false)
    private byte[] content;


    public void setExternalIds(List<ExternalId> externalIds) {
        this.externalIds = externalIds;
    }


    public List<ExternalId> getExternalIds() {
        return externalIds;
    }


    public byte[] getContent() {
        return content;
    }


    public void setContent(byte[] content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "EntryData{" +
                "externalIds=" + externalIds +
                ", content=" + Arrays.toString(content) +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntryData)) {
            return false;
        }

        EntryData entryData = (EntryData) o;

        if (getExternalIds() != null ? !getExternalIds().equals(entryData.getExternalIds()) : entryData.getExternalIds() != null) {
            return false;
        }
        return Arrays.equals(getContent(), entryData.getContent());
    }


    @Override
    public int hashCode() {
        int result = getExternalIds() != null ? getExternalIds().hashCode() : 0;
        result = 31 * result + Arrays.hashCode(getContent());
        return result;
    }
}
