package com.elys.pos.inventory.repository.v1;

import java.util.List;

public interface SoftDeleteRepository<T> {

    List<T> findAllActive();
}
