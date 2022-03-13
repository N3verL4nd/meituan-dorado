/*
 * Copyright 2018 Meituan Dianping. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meituan.dorado.demo.thrift.annotation;

import com.facebook.swift.codec.ThriftField;
import com.facebook.swift.codec.ThriftStruct;

@ThriftStruct
public final class TestRequest {

    private int userid;
    private String name;
    private String message;
    private int seqid;

    @ThriftField(1)
    public int getUserid() {
        return userid;
    }

    @ThriftField
    public void setUserid(int userid) {
        this.userid = userid;
    }

    @ThriftField(2)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 不指定修饰符, 默认就是 UNSPECIFIED; 此时可以省略掉 "value="
    @ThriftField(3)
    public String getMessage() {
        return message;
    }

    @ThriftField
    public void setMessage(String message) {
        this.message = message;
    }

    @ThriftField(4)
    public int getSeqid() {
        return seqid;
    }

    @ThriftField
    public void setSeqid(int seqid) {
        this.seqid = seqid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        return toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TestRequest");
        sb.append("{userid='").append(userid).append('\'');
        sb.append(", name=").append(name);
        sb.append(", message=").append(message);
        sb.append(", seqid=").append(seqid);
        sb.append('}');
        return sb.toString();
    }
}