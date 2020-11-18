function Effect() {
    var self = this;

    this.meshes = [
        { file: "hawaiiFlower.bsm2", anims: [
            { a: "CINEMA_4D_Main", t: 1000 },
        ] },
        { file: "BeautyFaceSP.bsm2", anims: [
            { a: "CINEMA_4D_Main", t: 1000 },
        ] },
    ];

    this.play = function() {
        var now = (new Date()).getTime();
        for(var i = 0; i < self.meshes.length; i++) {
            if(now > self.meshes[i].endTime) {
                self.meshes[i].animIdx = (self.meshes[i].animIdx + 1)%self.meshes[i].anims.length;
                Api.meshfxMsg("animOnce", i, 0, self.meshes[i].anims[self.meshes[i].animIdx].a);
                self.meshes[i].endTime = now + self.meshes[i].anims[self.meshes[i].animIdx].t;
            }
        }

        // if(isMouthOpen(world.landmarks, world.latents)) {
        //  Api.hideHint();
        // }
    };

    this.init = function() {
        Api.meshfxMsg("spawn", 2, 0, "!glfx_FACE");

        Api.meshfxMsg("spawn", 0, 0, "hawaiiFlower.bsm2");
        // Api.meshfxMsg("animOnce", 0, 0, "CINEMA_4D_Main");

        Api.meshfxMsg("spawn", 1, 0, "BeautyFaceSP.bsm2");
        // Api.meshfxMsg("animOnce", 1, 0, "CINEMA_4D_Main");

        if(Api.getPlatform() == "ios" || Api.getPlatform() == "iOS" || Api.getPlatform() == "macOS")
        {
            Api.meshfxMsg("blur", 0, 3);
            Api.meshfxMsg("spawn", 3, 0, "tri.bsm2");
        }

        Api.playVideo("foreground",true,1);
        for(var i = 0; i < self.meshes.length; i++) {
            self.meshes[i].animIdx = -1;
            self.meshes[i].endTime = 0;
        }

        self.faceActions = [self.play];
        // Api.showHint("Open mouth");
        // Api.playVideo("frx",true,1);

        Api.showRecordButton();
    };

    this.restart = function() {
        Api.meshfxReset();
        // Api.stopVideo("frx");
        self.init();
    };

    this.faceActions = [];
    this.noFaceActions = [];

    this.videoRecordStartActions = [];
    this.videoRecordFinishActions = [];
    this.videoRecordDiscardActions = [];
}

configure(new Effect());
