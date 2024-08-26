"use client"

import Image from "next/image"

import ViewInArIcon from "../../../../public/image/view_in_ar.png"
import styles from "./focus_task.module.css"
import Spacer from "@/app/(common)/(component)/(spacer)"
import {memo} from "react";
import {APIManager} from "@/app/(common)/(api)";

export default async function FocusTask() {
    const test = async () => {
        const  response = await APIManager.post({
            route: "/api/v1/login",
            body: {
                "username": "test",
                "password": "password123!",
            },
            //headers: { authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiY2F0ZWdvcnkiOiJBdXRob3JpemF0aW9uIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTcyNDY3ODQyNSwiZXhwIjoxNzI0NjgyMDI1fQ.v-BkKQjpNd23wiyrDE7gbCm_pzcqnHfowIKhmsAVWIg" }
        });
    }

    return (
        <div className={styles.container} onClick={test}>
            <span>집중공략</span>
            <Spacer spacing={10} direction="column"/>
            <div className={styles.content_container}>
                <Image
                src={ViewInArIcon}
                alt=""
                />
                <Spacer spacing={10} direction="column"/>
                <span>집중적으로 해결하고 싶은 일을 등록 해보세요!</span>
            </div>
        </div>
    )
}