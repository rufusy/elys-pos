package com.elys.pos.inventory.v1.repository;

import java.util.List;

public interface SoftDeleteRepository<T> {

    List<T> findAllActive();
}
