package io.service;

import io.repo.GroupRepo;
import io.repo.ModelRepo;
import qio.Qio;
import qio.annotate.Inject;
import qio.annotate.Service;

@Service
public class ModelService {

    @Inject
    Qio qio;

    @Inject
    ModelRepo modelRepo;

    @Inject
    GroupRepo groupRepo;

    @Inject
    SeaService seaService;

    @Inject
    AuthService authService;

    //-> TODO:
}
