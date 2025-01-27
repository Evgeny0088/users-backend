{{/*
common labels
*/}}
{{- define "common.labels" }}
app: {{ .Values.application.name }}
{{- end }}

{{/*
monitoring labels
*/}}
{{- define "monitoring.labels" -}}
{{ if .Values.monitoring.enabled }}
prometheus-monitored: "true"
{{- end }}
{{- end }}


