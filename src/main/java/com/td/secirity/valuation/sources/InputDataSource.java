package com.td.secirity.valuation.sources;

import java.io.IOException;
import java.util.List;

public interface InputDataSource<T> {

    List<T> getData();
}
