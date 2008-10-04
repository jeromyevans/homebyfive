package com.blueskyminds.enterprise.address;

import java.util.List;
import java.util.LinkedList;

/**
 * Date Started: 4/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class Addresses {

    private int sequenceNo;
    private List<AddressPath> list;

    public Addresses(int sequenceNo) {
        this.sequenceNo = sequenceNo;
        list = new LinkedList<AddressPath>();
    }    

    public Addresses(int sequenceNo, List<AddressPath> list) {
        this.sequenceNo = sequenceNo;
        this.list = list;
    }

    public Addresses() {
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public void add(AddressPath addressPath) {
        list.add(addressPath);
    }

    public List<AddressPath> getList() {
        return list;
    }

    public void setList(List<AddressPath> list) {
        this.list = list;
    }

    public int size() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }
}
