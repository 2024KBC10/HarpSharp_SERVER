import Spacer from "@/app/(common)/(component)/(spacer)"
import styles from "./regist_task_input_field.module.css"

export default function RegistTaskInputField({
    category,
    placeholder,
    onChange,
    isRequired = false,
    maxLength,
}: RegistTaskInputFieldProps) {
    return (
        <div className={styles.container}>
            <div className={styles.category_container}>
                <span>{category}</span>
                <Spacer spacing={2.5} direction="row"/>
                { isRequired ? <p>*</p> : <></> }
            </div>
            <Spacer spacing={5} direction="column"/>
            <input
            className={styles.input_container}
            placeholder={placeholder}
            onChange={(e) => onChange(e.target.value)}
            maxLength={maxLength}
            />
        </div>
    )
}

interface RegistTaskInputFieldProps {
    category: string
    placeholder: string
    onChange: (value: string) => void
    isRequired?: boolean
    maxLength?: number
}