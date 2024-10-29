package com.banuba.example.integrationapp;

import org.koin.core.module.Module;

public class SampleIntegrationKoinJavaModule {

    public Module getModule(){
        return new Module();
    }

    // here should be this implementation

//    val module = module {
//        single<ArEffectsRepositoryProvider>(createdAtStart = true) {
//            ArEffectsRepositoryProvider(
//                    arEffectsRepository = get(named("backendArEffectsRepository")),
//                    ioDispatcher = get(named("ioDispatcher"))
//            )
//        }
//
//        single<ContentFeatureProvider<TrackData, Fragment>>(
//                named("musicTrackProvider")
//        ) {
//            AudioBrowserMusicProvider()
//        }
//    }
}
