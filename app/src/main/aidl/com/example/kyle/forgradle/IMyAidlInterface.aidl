// IMyAidlInterface.aidl
package com.example.kyle.forgradle;

import com.example.kyle.forgradle.entity.EntityParcelable;

interface IMyAidlInterface {
            String helloAIDL();

            void updateServerInfo(in EntityParcelable entity);

            void dealWithClientInfo(out EntityParcelable entity);

            void useSameInfo(inout EntityParcelable entity);
}
